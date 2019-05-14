package com.s.android.hiandroid.ui.common.info

class StringListInfo {

    /**
     * 标题
     */
    var title: String = ""
    /**
     * 目标activity
     */
    var targetClass: Class<*>? = null
    /**
     * 调整webView url
     */
    var url: String? = null
}

fun getStringListInfo(title: String, url: String): StringListInfo {
    return StringListInfo().apply {
        this.title = title
        this.url = url
    }
}

fun getStringListInfo(title: String, targetClass: Class<*>): StringListInfo {
    return StringListInfo().apply {
        this.title = title
        this.targetClass = targetClass
    }
}
