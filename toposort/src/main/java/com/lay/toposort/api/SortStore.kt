package com.lay.toposort.api

import android.util.Log
import androidx.constraintlayout.solver.widgets.analyzer.Dependency

/**
 * 作者：qinlei on 2022/10/6 17:19
 */
class SortStore {

    private var result: MutableList<Class<out AndroidStartUp<*>>>? = null
    private var startupStore: MutableMap<Class<out AndroidStartUp<*>>, AndroidStartUp<*>>? = null
    private var dependency: MutableMap<Class<out AndroidStartUp<*>>, MutableList<Class<out AndroidStartUp<*>>>>? =
        null

    private val mainThread: MutableList<AndroidStartUp<*>> by lazy {
        mutableListOf()
    }
    private val ioThread: MutableList<AndroidStartUp<*>> by lazy {
        mutableListOf()
    }

    constructor()
    constructor(
        result: MutableList<Class<out AndroidStartUp<*>>>,
        startupStore: MutableMap<Class<out AndroidStartUp<*>>, AndroidStartUp<*>>,
        dependency: MutableMap<Class<out AndroidStartUp<*>>, MutableList<Class<out AndroidStartUp<*>>>>
    ) {
        this.result = result
        this.startupStore = startupStore
        this.dependency = dependency
    }

    fun getResult(): List<AndroidStartUp<*>> {

        if (result == null) return emptyList()

        val list = mutableListOf<AndroidStartUp<*>>()
        result?.forEach { key ->

            if (startupStore?.get(key)?.callOnMainThread()!!) {
                mainThread.add(startupStore?.get(key)!!)
            } else {
                ioThread.add(startupStore?.get(key)!!)
            }
        }
        list.addAll(ioThread)
        list.addAll(mainThread)
        return list
    }

    fun notify(task: AndroidStartUp<*>) {
        dependency?.get(task.javaClass)?.forEach {
            //通知全部依赖项
            Log.e("TAG", "node ${task.javaClass} -- $it")
            startupStore?.get(it)?.toCountDown()
        }
    }
}