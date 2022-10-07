package com.lay.toposort

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.lay.toposort.manager.StartupManager
import com.lay.toposort.provider.ProviderInitialize

/**
 * 作者：qinlei on 2022/10/7 08:34
 */
class StartupContentProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        Log.e("TAG","StartupContentProvider onCreate")
        context?.let {
            val list = ProviderInitialize.initialMetaData(it)
            StartupManager.Builder()
                .setAllTask(list)
                .builder().start(it).await()
        }
        return false
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return null
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }
}