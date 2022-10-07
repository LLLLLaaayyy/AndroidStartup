package com.lay.toposort.dispatch

import java.util.concurrent.Executor

/**
 * 作者：qinlei on 2022/10/6 21:33
 */
interface IDispatcher {

    fun callOnMainThread():Boolean //是否在主线程中执行
    fun waitOnMainThread():Boolean //是否需要等待该任务完成
    fun toWait(){} //等待父任务执行完成
    fun toCountDown(){} //父任务执行完成
    fun executor():Executor //线程池
    fun threadPriority():Int //线程优先级
}