package com.s.android.hiandroid.ui.android.wifi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.SupplicantState
import android.net.wifi.WifiManager
import com.s.android.hiandroid.common.utils.logD

class WifiBroadCastReceiver(private val wifiListener: WifiListener) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            WifiManager.WIFI_STATE_CHANGED_ACTION -> {
                // wifi开关变化通知
                logD("wifi开关变化通知")
                val wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED)
                wifiListener.wifiStateChanged(wifiState)
            }
            WifiManager.SCAN_RESULTS_AVAILABLE_ACTION -> {
                // wifi扫描结果通知
                logD("wifi扫描结果通知")
                wifiListener.wifiScanComplete()
            }
            WifiManager.SUPPLICANT_STATE_CHANGED_ACTION -> {
                // wifi连接结果通知
                val state: SupplicantState = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE)
                logD("wifi连接结果通知 : $state")
                wifiListener.wifiConnectionChanged(state)
            }
            WifiManager.NETWORK_STATE_CHANGED_ACTION -> {
                // 网络状态变化通知
//                logD("网络状态变化通知")
            }
            else -> {
            }
        }
    }
}

interface WifiListener {

    fun wifiStateChanged(state: Int)

    fun wifiScanComplete()

    fun wifiConnectionChanged(state: SupplicantState)
}