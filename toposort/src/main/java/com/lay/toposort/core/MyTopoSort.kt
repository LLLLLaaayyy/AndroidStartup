package com.lay.toposort.core

import android.util.Log
import com.lay.toposort.api.AbsAndroidStartUp
import com.lay.toposort.api.AndroidStartUp
import com.lay.toposort.api.SortStore
import kotlin.collections.containsKey as containsKey1

/**
 * 作者：qinlei on 2022/10/6 11:31
 */
class MyTopoSort {

    private val inDegree: MutableMap<Class<out AndroidStartUp<*>>, Int> by lazy {
        mutableMapOf()
    }

    private val nodeDependency: MutableMap<Class<out AndroidStartUp<*>>, MutableList<Class<out AndroidStartUp<*>>>> by lazy {
        mutableMapOf()
    }

    private val startupStore: MutableMap<Class<out AndroidStartUp<*>>, AndroidStartUp<*>> by lazy {
        mutableMapOf()
    }

    //存储顶点
    private val queue: ArrayDeque<Class<out AndroidStartUp<*>>> by lazy {
        ArrayDeque()
    }


    fun sort(map: List<AndroidStartUp<*>>?):SortStore {

        if (map == null) {
            return SortStore()
        }

        //遍历全部的节点
        map.forEach { node ->
            //记录入度数
            inDegree[node.javaClass] = node.getDependencyCount()
            startupStore[node.javaClass] = node

            if (node.getDependencyCount() == 0) {
                //查找到顶点
                queue.addLast(node.javaClass)
            } else {
                //如果不是顶点需要查找依赖关系，找到每个节点对应的边
                //例如node == task2 依赖 task1
                // task1 -- task2就是一条边，因此需要拿task1作为key，存储这条边
                // task1 -- task3也是一条边，在遍历到task3的时候，也需要存进来
                node.dependencies()?.forEach { parent ->

                    var list = nodeDependency[parent]
                    if (list == null) {
                        list = mutableListOf()
                        nodeDependency[parent] = list
                    }
                    list.add(node.javaClass)
                }
            }
        }

        val result = mutableListOf<Class<out AndroidStartUp<*>>>()
        //依次删除顶点
        while (queue.isNotEmpty()) {
            //取出顶点
            val node = queue.removeFirst()
            Log.e("TAG", "取出顶点--$node")
            result.add(node)
            //查找依赖关系，凡是依赖该顶点的，入度数都 -1
            if (nodeDependency.containsKey(node)) {
                val nodeList = nodeDependency[node]
                nodeList!!.forEach { node ->
                    val degree = inDegree[node]
                    inDegree[node] = degree!! - 1
                    if (degree - 1 == 0) {
                        queue.add(node)
                    }
                }
            }
        }
        return SortStore(result,startupStore,nodeDependency)
    }
}