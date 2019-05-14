package com.s.android.hiandroid.ui.android

import com.s.android.hiandroid.ui.android.bluetooth.BluetoothListActivity
import com.s.android.hiandroid.ui.android.bus.BusActivity
import com.s.android.hiandroid.ui.android.vpn.VPNActivity
import com.s.android.hiandroid.ui.android.wifi.WiFiActivity
import com.s.android.hiandroid.ui.common.BaseStringListActivity
import com.s.android.hiandroid.ui.common.info.StringListInfo
import com.s.android.hiandroid.ui.common.info.getStringListInfo

class AndroidActivity : BaseStringListActivity() {

    override fun getItems(): MutableList<StringListInfo> {
        return mutableListOf(
            getStringListInfo("Bus", BusActivity::class.java),
            getStringListInfo("Bluetooth", BluetoothListActivity::class.java),
            getStringListInfo("VPN", VPNActivity::class.java),
            getStringListInfo("WiFi", WiFiActivity::class.java)
        )
    }

}
