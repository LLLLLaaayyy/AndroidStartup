package com.lay.toposort.manager

import android.content.Context
import android.os.Looper
import android.util.Log
import com.lay.toposort.api.AndroidStartUp
import com.lay.toposort.api.Result
import com.lay.toposort.api.SortStore
import com.lay.toposort.core.MyTopoSort
import com.lay.toposort.executor.StartupRunnable
import java.lang.IllegalArgumentException
import java.util.concurrent.CountDownLatch

/**
 * 作者：qinlei on 2022/10/6 17:04
 */
class StartupManager {

    private var list: List<AndroidStartUp<*>>? = null
    private var sortStore: SortStore? = null

    private val mainCountDownLatch by lazy {
        CountDownLatch(list!!.size)
    }

    constructor(list: List<AndroidStartUp<*>>) {
        this.list = list
    }

    fun start(context: Context): StartupManager {
        //判断是否在主线程中执行
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw IllegalArgumentException("请在主线程中使用该框架")
        }
        //排序
        sortStore = MyTopoSort().sort(list)
        sortStore?.getResult()?.forEach { task ->

            val runnable = StartupRunnable(context, task, this)
            //判断当前任务执行的线程
            if (task.callOnMainThread()) {
                //如果在主线程，那么就直接执行
                Log.e("TAG","$task callOnMainThread")
                runnable.run()
            } else {
                //如果在子线程
                task.executor().execute(runnable)
                Log.e("TAG","$task callOnIOThread")
            }
        }
        return this
    }

    fun notifyChildren(task: AndroidStartUp<*>) {
        sortStore?.notify(task)
        //每次唤醒一个任务，就需要countdown
        mainCountDownLatch.countDown()
    }

    fun await() {
        mainCountDownLatch.await()
    }


    class Builder {

        private val list: MutableList<AndroidStartUp<*>> by lazy {
            mutableListOf()
        }

        fun setTask(task: AndroidStartUp<*>): Builder {
            list.add(task)
            return this
        }

        fun setAllTask(tasks: List<AndroidStartUp<*>>): Builder {
            list.addAll(tasks)
            return this
        }

        fun builder(): StartupManager {
            return StartupManager(list)
        }

    }
}