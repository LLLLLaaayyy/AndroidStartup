package com.lay.toposort.provider

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import com.lay.toposort.api.AndroidStartUp

/**
 * 作者：qinlei on 2022/10/7 08:38
 */
object ProviderInitialize {

    private const val META_KEY = "app_startup"
    fun initialMetaData(context: Context): List<AndroidStartUp<*>> {

        val provider = ComponentName(context, "com.lay.toposort.StartupContentProvider")
        val providerInfo =
            context.packageManager.getProviderInfo(provider, PackageManager.GET_META_DATA)

        //存储节点
        val nodeMap: MutableMap<Class<*>, AndroidStartUp<*>> = mutableMapOf()
        providerInfo.metaData.keySet().forEach { key ->
            Log.e("TAG", "key ===> $key")
            val dataKey = providerInfo.metaData.get(key)
            Log.e("TAG", "dataKey ===> $dataKey")
            if (dataKey == META_KEY) {
                //处理task
                doInitializeTask(context, Class.forName(key), nodeMap)
            }
        }

        return ArrayList(nodeMap.values)
    }

    private fun doInitializeTask(
        context: Context,
        clazz: Class<*>,
        nodeMap: MutableMap<Class<*>, AndroidStartUp<*>>
    ) {
        val startUp = clazz.newInstance() as AndroidStartUp<*>
        Log.e("TAG", "clazz ===> $clazz")
        if (!nodeMap.containsKey(clazz)) {
            nodeMap[clazz] = startUp
        }
        //查找依赖项
        if (startUp.dependencies() != null) {
            //获取全部的依赖项
            val dependencyList = startUp.dependencies()
            dependencyList?.forEach {
                doInitializeTask(context, it, nodeMap)
            }
        }
    }

}