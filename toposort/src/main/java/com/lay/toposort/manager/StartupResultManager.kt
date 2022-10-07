package com.lay.toposort.manager

import com.lay.toposort.api.AndroidStartUp
import com.lay.toposort.api.Result
import java.util.concurrent.ConcurrentHashMap

/**
 * 作者：qinlei on 2022/10/6 16:49
 * 用于存储每个任务执行返回的结果
 */
class StartupResultManager {

    private val result: ConcurrentHashMap<Class<out AndroidStartUp<*>>, Result<*>> by lazy {
        ConcurrentHashMap()
    }

    fun saveResult(key: Class<out AndroidStartUp<*>>, value: Result<*>) {
        result[key] = value
    }

    fun getResult(key: Class<out AndroidStartUp<*>>): Result<*>? {
        return result[key]
    }

    companion object {
        val instance: StartupResultManager by lazy {
            StartupResultManager()
        }
    }
}