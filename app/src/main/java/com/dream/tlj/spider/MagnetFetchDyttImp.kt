package com.dream.tlj.spider

import com.dream.tlj.cache.CacheHttpUtils
import com.dream.tlj.mvp.e.MagnetInfo
import com.dream.tlj.mvp.e.MagnetRule
import com.orhanobut.logger.Logger
import org.htmlcleaner.CleanerProperties
import org.htmlcleaner.DomSerializer
import org.htmlcleaner.HtmlCleaner
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*
import java.util.regex.Pattern
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

class MagnetFetchDyttImp : MagnetFetchInf() {
    override fun parser(rule: com.dream.tlj.mvp.e.MagnetRule, keyword: String, page: Int): List<com.dream.tlj.mvp.e.MagnetInfo> {
        val newUrl = transformUrl(rule.source, keyword, transformPage(page))
        Logger.d("==========="+newUrl)
//        val html = Jsoup.connect(newUrl).validateTLSCertificates(false).get().body().html()
        val html = CacheHttpUtils.get(newUrl)
        val xPath = XPathFactory.newInstance().newXPath()
        val tagNode = HtmlCleaner().clean(html)
        val dom = DomSerializer(CleanerProperties()).createDOM(tagNode)
        val result = xPath.evaluate(rule.list, dom, XPathConstants.NODESET) as NodeList
        val infos = ArrayList<com.dream.tlj.mvp.e.MagnetInfo>()

        for (i in 0 until result.length) {
            val node = result.item(i)
            if (node != null) {
                val nameNote:Node? = xPath.evaluate(rule.name, node, XPathConstants.NODE)as? Node
                val name = nameNote?.textContent

                val descNode:Node? = xPath.evaluate(rule.desc, node, XPathConstants.NODE)as? Node
                val desc = descNode?.textContent?.trim()

                val detailNode:Node? = nameNote?.getAttributes()?.getNamedItem("href");
                val detailUrl = detailNode?.textContent

                infos.add(com.dream.tlj.mvp.e.MagnetInfo(name, null, rule.detailBaseUrl + detailUrl, desc))
            }
        }
        return infos
    }


    override fun transformPage(page: Int?): Int {
        return if (page == null || page <= 0) 1 else page+1
    }

    private fun transformUrl(url: String, keyword: String, page: Int?): String {
        try {
            return String.format(url, URLEncoder.encode(keyword, "gb2312"), page)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        return ""
    }

    /**
     * 磁力链转换
     *
     * @param url
     * @return
     */
    private fun transformMagnet(url: String): String {
        val regex = "magnet:?[^\\\"]+"
        val matches = Pattern.matches(regex, url)
        if (matches) {
            return url
        } else {
            var newMagnet: String
            try {
                val sb = StringBuffer(url)
                val htmlIndex = url.lastIndexOf(".html")
                if (htmlIndex != -1) {
                    sb.delete(htmlIndex, sb.length)
                }
                val paramIndex = url.indexOf("&")
                if (paramIndex != -1) {
                    sb.delete(paramIndex, sb.length)
                }
                val paramIndex2 = url.indexOf("?r")
                if (paramIndex2 != -1) {
                    sb.delete(paramIndex2, sb.length)
                }
                if (sb.length >= 40) {
                    newMagnet = sb.substring(sb.length - 40, sb.length)
                } else {
                    newMagnet = url
                }
            } catch (e: Exception) {
                e.printStackTrace()
                newMagnet = url
            }

            return String.format("magnet:?xt=urn:btih:%s", newMagnet)
        }
    }
}