package com.lay.toposort.executor

import android.content.Context
import android.os.Process
import android.util.Log
import com.lay.toposort.api.AndroidStartUp
import com.lay.toposort.api.Result
import com.lay.toposort.manager.StartupManager
import com.lay.toposort.manager.StartupResultManager

/**
 * 作者：qinlei on 2022/10/6 21:57
 */
class StartupRunnable : Runnable {

    private var task: AndroidStartUp<*>? = null
    private var context: Context? = null
    private var manager:StartupManager? = null

    constructor(context: Context, task: AndroidStartUp<*>,manager: StartupManager) {
        this.task = task
        this.context = context
        this.manager = manager
    }

    override fun run() {
        Process.setThreadPriority(task?.threadPriority() ?: Process.THREAD_PRIORITY_DEFAULT)
        Log.e("TAG","${task?.javaClass}")
        //当前任务暂停执行
        task?.toWait() //注意，如果是顶点，状态值为0，那么就不会阻塞
        //等到依赖的任务全部执行完成，再执行
        val result = task?.createTask(context!!)
        StartupResultManager.instance.saveResult(task?.javaClass!!, Result(result))
        //当前任务执行完成，通知子任务
        manager?.notifyChildren(task!!)
    }
}