package cn.sddman.download.mvp.p

import cn.sddman.download.common.Const
import cn.sddman.download.mvp.e.DownloadTaskEntity
import cn.sddman.download.mvp.m.DownLoadModel
import cn.sddman.download.mvp.m.DownLoadModelImp
import cn.sddman.download.mvp.m.TaskModel
import cn.sddman.download.mvp.m.TaskModelImp
import cn.sddman.download.mvp.v.DownLoadIngView
import cn.sddman.download.util.AppSettingUtil
import cn.sddman.download.util.SystemConfig
import cn.sddman.download.view.DownProgressNotify

class DownloadIngPresenterImp(private val downLoadIngView: DownLoadIngView) : DownloadIngPresenter {
    private val taskModel: TaskModel
    private val downLoadModel: DownLoadModel
    override val downloadingTaskList: List<DownloadTaskEntity>?
    private val isLoop = true

    init {
        taskModel = TaskModelImp()
        downLoadModel = DownLoadModelImp()
        downloadingTaskList = taskModel.findLoadingTask()
        //downLoadIngView.initTaskListView(list);
        //refreshData();
    }

    override fun startTask(task: DownloadTaskEntity) {
        val netType = SystemConfig.netType
        if (netType == Const.NET_TYPE_UNKNOW) {
            downLoadIngView.alert("没有网络,下载暂停", Const.ERROR_ALERT)
            return
        } else if ((!AppSettingUtil.instance.isMobileNetDown!!) && netType == Const.NET_TYPE_MOBILE) {
            downLoadIngView.alert("设置不允许允许流量下载,请在设置里开启流量下载", Const.ERROR_ALERT)
            return
        }
        val downCount = AppSettingUtil.instance.downCount
        val downs = taskModel.findDowningTask()
        if (downCount <= downs!!.size) {
            task.setmTaskStatus(Const.DOWNLOAD_WAIT)
            taskModel.updateTask(task)
            return
        }
        //List<DownloadTaskEntity> tasks=taskModel.findLoadingTask();
        val b = downLoadModel.startTask(task)!!
        if (!b)
            downLoadIngView.alert("开始任务失败,无法获取下载资源,可尝试多点几次开始任务", Const.ERROR_ALERT)
    }

    override fun stopTask(task: DownloadTaskEntity) {
        downLoadModel.stopTask(task)
    }

    override fun deleTask(task: DownloadTaskEntity, deleFile: Boolean) {
        downLoadModel.deleTask(task, true, deleFile)
        DownProgressNotify.instance.cancelDownProgressNotify(task)
    }

    override fun refreshData() {

    }

    override fun stopLoop() {

    }

    override fun clearHandler() {

    }
}
