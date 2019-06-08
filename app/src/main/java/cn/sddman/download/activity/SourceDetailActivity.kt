package cn.sddman.download.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import cn.sddman.download.R
import cn.sddman.download.adapter.SourceDetailListAdapter
import cn.sddman.download.common.BaseActivity
import cn.sddman.download.common.Const
import cn.sddman.download.mvp.e.MagnetDetail
import cn.sddman.download.mvp.e.MagnetRule
import cn.sddman.download.mvp.p.SourceDetailPresenterImp
import cn.sddman.download.mvp.p.UrlDownLoadPresenterImp
import cn.sddman.download.mvp.v.SourceDetailView
import cn.sddman.download.mvp.v.UrlDownLoadView
import cn.sddman.download.util.Util
import kotlinx.android.synthetic.main.activity_torrent_info.*

class SourceDetailActivity : BaseActivity(), SourceDetailView, UrlDownLoadView {


    private var detailUrl: String? = null
    private lateinit var magnetRule:MagnetRule
    private lateinit var sourceDetailListAdapter: SourceDetailListAdapter

    private var linkList = arrayListOf<MagnetDetail>()

    companion object {
        val DETAIL_URL:String = "detail_url"
        val TITLE:String = "title"
        val MAGNET_RULE:String = "magnet_rule"
    }
    override fun refreshData(list: List<MagnetDetail>) {
        linkList.clear()
        linkList.addAll(list)
        sourceDetailListAdapter.notifyDataSetChanged()
    }

    override fun clickItem(url: String) {


    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_source_detail)
        val getIntent = intent
        detailUrl = getIntent.getStringExtra(DETAIL_URL)
        val title = getIntent.getStringExtra(TITLE)
        magnetRule = getIntent.getParcelableExtra(MAGNET_RULE)
        setTopBarTitle(title)
        val sourceDetailPresenterImp = SourceDetailPresenterImp(this)
        urlDownLoadPresenter = UrlDownLoadPresenterImp(this)
        sourceDetailPresenterImp.parser(magnetRule,detailUrl!!)
        initRV()

    }

    private fun initRV(){
        val manager = LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false)
        recyclerview!!.layoutManager = manager
        sourceDetailListAdapter = SourceDetailListAdapter(this, this, this.linkList)
        recyclerview.adapter = sourceDetailListAdapter
    }

    fun selectAllClick(v: View){
        for(x in linkList){
            x.check = true
        }
        sourceDetailListAdapter.notifyDataSetChanged()
    }

    private lateinit var urlDownLoadPresenter: UrlDownLoadPresenterImp

    fun downloadSelectedClick(v: View){
        for (x in linkList){
            if (x.check){
                urlDownLoadPresenter.startTask(x.name)
            }
        }
    }

    override fun addTaskSuccess() {
        Util.alert(this, "添加任务成功", Const.SUCCESS_ALERT)
    }

    override fun addTaskFail(msg: String) {
        Util.alert(this, msg, Const.ERROR_ALERT)
    }

}


