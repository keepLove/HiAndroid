package com.s.android.hiandroid.common.utils

import android.content.Context
import android.support.annotation.ArrayRes
import com.s.android.hiandroid.ui.common.info.StringListInfo
import com.s.android.hiandroid.ui.common.info.getStringListInfo

fun Context.getStringArray(@ArrayRes id: Int): MutableList<String> {
    return resources.getStringArray(id).toMutableList()
}

fun Context.getStringListInfos(@ArrayRes id: Int, clazz: Class<*>): MutableList<StringListInfo> {
    return getStringArray(id).map { getStringListInfo(it, clazz) }.toMutableList()
}
