package com.s.android.hiandroid.common.utils

import android.content.Context
import android.support.annotation.ArrayRes

fun Context.getStringArray(@ArrayRes id: Int): MutableList<String> {
    return resources.getStringArray(id).toMutableList()
}
