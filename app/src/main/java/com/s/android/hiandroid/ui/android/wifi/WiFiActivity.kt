package com.s.android.hiandroid.ui.android.wifi

import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentFilter
import android.net.wifi.*
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseViewHolder
import com.s.android.hiandroid.R
import com.s.android.hiandroid.common.BaseRecyclerAdapter
import com.s.android.hiandroid.common.utils.logD
import com.s.android.hiandroid.common.utils.showToast
import com.s.android.hiandroid.common.utils.showToastAndLog
import com.s.android.hiandroid.ui.common.BaseActivity
import kotlinx.android.synthetic.main.activity_wi_fi.*

class WiFiActivity : BaseActivity(), WifiListener {

    private val wifiBroadCastReceiver = WifiBroadCastReceiver(this)
    private var mAdapter: BaseRecyclerAdapter<ScanResult>? = null
    private var connectionWifi: WifiInfo? = null
    private var wifiConfiguration: List<WifiConfiguration>? = null
    private val wifiManager: WifiManager by lazy { applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager }
    private var connectionWifiPosition: Int = 0

    override fun getLayoutResID(): Int? {
        return R.layout.activity_wi_fi
    }

    override fun init(savedInstanceState: Bundle?) {
        registerReceiver()
        switch1.isChecked = wifiManager.isWifiEnabled
        connectionWifi = wifiManager.connectionInfo
        wifiConfiguration = wifiManager.configuredNetworks
        wifiStateChanged(wifiManager.wifiState)
        switch1.setOnCheckedChangeListener { _, isChecked ->
            wifiManager.isWifiEnabled = isChecked
        }
        bt_scan.setOnClickListener {
            showProgressDialog()
            wifiManager.startScan()
        }
        bt_cancel.setOnClickListener {
            ll_connection.visibility = View.GONE
        }
        bt_connection.setOnClickListener {
            mAdapter?.getItem(connectionWifiPosition)?.let {
                if (it.BSSID == connectionWifi?.bssid) {
                    ll_connection.visibility = View.GONE
                    showToastAndLog("已连接")
                    return@setOnClickListener
                }
                showProgressDialog()
                val wifiConfiguration1 = createConfiguration(it)
                //如果你设置的wifi是设备已经存储过的，那么这个networkId会返回小于0的值。
                var networkId = wifiManager.addNetwork(wifiConfiguration1)
                logD("addNetwork id: $networkId")
                if (networkId < 0) {
                    val view: TextView? =
                        mAdapter!!.getViewByPosition(recycler_view, connectionWifiPosition, R.id.tv_level) as TextView?
                    networkId = view?.text?.toString()?.toIntOrNull() ?: -1
                }
                logD("enableNetwork id: $networkId")
                if (networkId < 0 || !wifiManager.enableNetwork(networkId, true)) {
                    dismissProgressDialog()
                    showToast("连接失败")
                }
            }
        }
        bt_disconnection.setOnClickListener {
            wifiManager.disconnect()
        }
        initRecyclerView(wifiManager)
    }

    private fun createConfiguration(scanResult: ScanResult): WifiConfiguration {
        val config = WifiConfiguration()
        config.SSID = "\"" + scanResult.SSID + "\""

        val encryptionType = scanResult.capabilities
        val password = et_password.text.toString()
        when {
            encryptionType.contains("wep", true) -> {
                /**
                 * special handling according to password length is a must for wep
                 */
                val i = password.length
                if (((i == 10 || (i == 26) || (i == 58))) && (password.matches(Regex("[0-9A-Fa-f]*")))) {
                    config.wepKeys[0] = password
                } else {
                    config.wepKeys[0] = "\"" + password + "\""
                }
                config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.SHARED)
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN)
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE)
                config.wepTxKeyIndex = 0
            }
            encryptionType.contains("wpa", true) -> {
                config.preSharedKey = "\"" + password + "\""
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK)
            }
            else -> config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE)
        }
        return config
    }

    private fun initRecyclerView(wifiManager: WifiManager) {
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))
        mAdapter = object : BaseRecyclerAdapter<ScanResult>(R.layout.item_wifi_info, wifiManager.scanResults) {
            override fun convert(helper: BaseViewHolder, item: ScanResult) {
                helper.setText(R.id.tv_name, item.SSID)
                    .setText(R.id.tv_start, getStart(helper, item))
//                    .setText(R.id.tv_level, item.level.toString())
            }
        }
        mAdapter?.setOnItemClickListener { _, _, position ->
            connectionWifiPosition = position
            ll_connection.visibility = View.VISIBLE
        }
        recycler_view.adapter = mAdapter
    }

    private fun getStart(helper: BaseViewHolder, scanResult: ScanResult): String {
        val stringBuilder = StringBuilder()
        if (scanResult.BSSID == connectionWifi?.bssid) {
            stringBuilder.append("已连接")
        } else {
            wifiConfiguration?.forEach {
                // configuration 可能不存在BSSID，SSID存在""
                if (it.SSID == "\"${scanResult.SSID}\"") {
                    stringBuilder.append("已保存")
                    helper.setText(R.id.tv_level, it.networkId.toString())
                    return@forEach
                }
            }
        }
        if (stringBuilder.isNotEmpty()) {
            stringBuilder.append("，")
        }
        stringBuilder
            .append("BSSID:${scanResult.BSSID}")
            .append("，")
            .append("capabilities:${scanResult.capabilities}")
            .append("，")
            .append(
                "频率:${when {
                    scanResult.frequency in 4901..5899 -> "5GHz"
                    scanResult.frequency in 2401..2499 -> "24GHz"
                    else -> scanResult.frequency.toString() + "MHz"
                }}"
            )
            .append("，")
            .append("信号等级:${WifiManager.calculateSignalLevel(scanResult.level, 5)}")
        return stringBuilder.toString()
    }

    override fun wifiScanComplete() {
        dismissProgressDialog()
        mAdapter?.setNewData(wifiManager.scanResults)
    }

    override fun wifiConnectionChanged(state: SupplicantState) {
        if (state == SupplicantState.COMPLETED || state == SupplicantState.DISCONNECTED) {
            dismissProgressDialog()
            ll_connection.visibility = View.GONE
            connectionWifi = wifiManager.connectionInfo
            wifiConfiguration = wifiManager.configuredNetworks
            mAdapter?.notifyDataSetChanged()
        }
    }

    private fun isConnecting(state: SupplicantState): Boolean {
        return when (state) {
            SupplicantState.AUTHENTICATING, SupplicantState.ASSOCIATING,
            SupplicantState.ASSOCIATED, SupplicantState.FOUR_WAY_HANDSHAKE,
            SupplicantState.GROUP_HANDSHAKE, SupplicantState.COMPLETED -> true
            SupplicantState.DISCONNECTED, SupplicantState.INTERFACE_DISABLED,
            SupplicantState.INACTIVE, SupplicantState.SCANNING,
            SupplicantState.DORMANT, SupplicantState.UNINITIALIZED, SupplicantState.INVALID -> false
            else -> throw IllegalArgumentException("Unknown supplicant state")
        }
    }

    @SuppressLint("SetTextI18n")
    override fun wifiStateChanged(state: Int) {
        when (state) {
            WifiManager.WIFI_STATE_DISABLED -> {
                tvWifiState.text = "WiFi state：wifi关闭"
            }
            WifiManager.WIFI_STATE_DISABLING -> {
                tvWifiState.text = "WiFi state：wifi正在关闭"
            }
            WifiManager.WIFI_STATE_ENABLING -> {
                tvWifiState.text = "WiFi state：wifi正在开启"
            }
            WifiManager.WIFI_STATE_ENABLED -> {
                tvWifiState.text = "WiFi state：wifi开启"
            }
            WifiManager.WIFI_STATE_UNKNOWN -> {
                tvWifiState.text = "WiFi state：wifi未知"
            }
            else -> {
                tvWifiState.text = "WiFi state：wifi未知"
            }
        }
    }

    private fun registerReceiver() {
        val intentFilter = IntentFilter()
        // wifi开关变化通知
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
        // wifi扫描结果通知
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        // wifi连接结果通知
        intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)
        // 网络状态变化通知
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
        registerReceiver(wifiBroadCastReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(wifiBroadCastReceiver)
    }
}
