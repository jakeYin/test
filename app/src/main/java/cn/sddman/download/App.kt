package cn.sddman.download

import android.app.Application
import android.content.pm.PackageManager
import android.util.Log

import com.xunlei.downloadlib.XLTaskHelper

import org.xutils.x

import cn.sddman.download.common.DelegateApplicationPackageManager

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        x.Ext.init(this)
        //x.Ext.setDebug(BuildConfig.DEBUG);
        XLTaskHelper.init(applicationContext)
        instance = this
    }

    override fun getPackageName(): String {
        return if (Log.getStackTraceString(Throwable()).contains("com.xunlei.downloadlib")) {
            "com.xunlei.downloadprovider"
        } else super.getPackageName()
    }

    override fun getPackageManager(): PackageManager {
        return if (Log.getStackTraceString(Throwable()).contains("com.xunlei.downloadlib")) {
            DelegateApplicationPackageManager(super.getPackageManager())
        } else super.getPackageManager()
    }

    companion object {
        var instance: App? = null
        fun appInstance(): App? {
            return instance
        }
    }
}
