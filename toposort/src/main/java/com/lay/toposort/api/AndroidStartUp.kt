package com.lay.toposort.api

import android.content.Context
import com.lay.toposort.dispatch.IDispatcher

/**
 * 作者：qinlei on 2022/10/6 10:49
 */
interface AndroidStartUp<T>:IDispatcher {

    //创建任务
    fun createTask(context: Context):T

    //依赖的任务
    fun dependencies():List<Class<out AndroidStartUp<*>>>?

    //入度数
    fun getDependencyCount():Int
}