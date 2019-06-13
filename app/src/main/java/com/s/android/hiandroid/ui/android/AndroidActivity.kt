package com.s.android.hiandroid.ui.android

import com.s.android.hiandroid.ui.android.aidl.AIDLActivity
import com.s.android.hiandroid.ui.android.bluetooth.BluetoothListActivity
import com.s.android.hiandroid.ui.android.bus.BusActivity
import com.s.android.hiandroid.ui.android.customview.CustomViewActivity
import com.s.android.hiandroid.ui.android.touch.NestedScrollingActivity
import com.s.android.hiandroid.ui.android.vpn.VPNActivity
import com.s.android.hiandroid.ui.android.wifi.WiFiActivity
import com.s.android.hiandroid.ui.common.BaseStringListActivity
import com.s.android.hiandroid.ui.common.info.StringListInfo
import com.s.android.hiandroid.ui.common.info.getStringListInfo

class AndroidActivity : BaseStringListActivity() {

    override fun getItems(): MutableList<StringListInfo> {
        return mutableListOf(
            getStringListInfo("自定义View", CustomViewActivity::class.java),
            getStringListInfo("Touch", NestedScrollingActivity::class.java),
            getStringListInfo("Bus", BusActivity::class.java),
            getStringListInfo("Bluetooth", BluetoothListActivity::class.java),
            getStringListInfo("VPN", VPNActivity::class.java),
            getStringListInfo("WiFi", WiFiActivity::class.java),
            getStringListInfo("AIDL", AIDLActivity::class.java),
            getStringListInfo(
                "自定义权限",
                "https://mp.weixin.qq.com/s?__biz=MzIxNzU1Nzk3OQ==&mid=2247487197&idx=1&sn=1abf3bff6481d6b25511fc7043a13bc3&chksm=97f6b069a081397fd5f739b3a8ce8a5b245ee02f1b5899b556d9467720870768828f6f095082&scene=38#wechat_redirect"
            )
        )
    }

}
