package com.s.android.hiandroid

import android.app.Application
//import com.jeremyliao.liveeventbus.LiveEventBus
import com.s.android.hiandroid.common.utils.LogUtils

class App : Application() {

    companion object {
        lateinit var app: App
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        LogUtils.init(this)
//        LiveEventBus.get()
//            .config()
//            .lifecycleObserverAlwaysActive(false)
    }
}