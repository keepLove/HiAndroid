@file:JvmName("LogUtil")

package com.s.android.hiandroid.common.utils

import android.content.Context
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.s.android.hiandroid.BuildConfig
import com.s.android.hiandroid.R


/**
 * Created by Administrator on 2019/3/8.
 * Log的工具封装
 */
object LogUtils {

    /**
     * 初始化.固定Tag.全级别Log,隐藏线程信息.显示行数
     */
    fun init(context: Context) {
        val tag = context.resources.getString(R.string.app_name)
        Logger.addLogAdapter(object :
                AndroidLogAdapter(PrettyFormatStrategy.newBuilder().tag(tag).methodCount(5).build()) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }
}

/**
 * 需要使用通配符来拼接参数
 * 如logI("使用通配符%s%s",1,2)
 * @param log  log主体,中间需要使用通配符才能拼接参数
 * *
 * @param args 参数
 */
fun logI(log: String, vararg args: Any) {
    Logger.i(log, *args)
}

/**
 * Info级的Log
 * @param log 需要输入的内容
 */
fun logI(log: Any) {
    Logger.i("%s", log)
}

/**
 * Error级的log

 * @param e 异常
 */
fun logE(e: Throwable) {
    Logger.e(e, "异常", "")
}

/**
 * Error级的log

 * @param e 异常
 */
fun logE(message: String, e: Throwable) {
    Logger.e(e, message, "")
}

/**
 * Error级的log,内容需要有通配符才能匹配后面的参数
 * @param log  内容
 * *
 * @param args 参数
 */
fun logE(log: String, vararg args: Any) {
    Logger.e(log, *args)
}

/**
 * Error级的log
 * @param log 需要输入的内容
 */
fun logE(log: Any) {
    Logger.e("%s", log)
}

/**
 * 打印Json
 * @param log 内容
 */
fun logJson(log: String) {
    com.orhanobut.logger.Logger.d(log)
}

/**
 * debug级的Log
 * @param log 需要输入的内容
 */
fun logD(log: Any) {
    Logger.i("%s", log)
}

/**
 * debug级的Log
 * @param s 需要输入的内容
 */
fun logD(s: String) {
    Logger.d(s)
}
