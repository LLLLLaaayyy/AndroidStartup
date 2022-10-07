package com.lay.toposort.api

import android.content.Context
import android.os.Process
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory

/**
 * 作者：qinlei on 2022/10/6 11:02
 */
abstract class AbsAndroidStartUp<T> : AndroidStartUp<T> {

    //依赖的任务数的个数作为状态值
    private val countDownLatch = CountDownLatch(getDependencyCount())

    override fun dependencies(): List<Class<out AndroidStartUp<*>>>? {
        return null
    }

    override fun getDependencyCount(): Int {
        return if (dependencies() != null) dependencies()!!.size else 0
    }

    override fun executor(): Executor {
        return Executors.newFixedThreadPool(5)
    }

    override fun toWait() {
        countDownLatch.await()
    }

    override fun toCountDown() {
        countDownLatch.countDown()
    }

    override fun threadPriority(): Int {
        return Process.THREAD_PRIORITY_DEFAULT
    }
}