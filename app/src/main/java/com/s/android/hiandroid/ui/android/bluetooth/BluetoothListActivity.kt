package com.s.android.hiandroid.ui.android.bluetooth

import android.content.Intent
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.s.android.hiandroid.R
import com.s.android.hiandroid.common.utils.getStringArray
import com.s.android.hiandroid.ui.common.BaseStringListActivity
import com.s.android.hiandroid.ui.common.OptionsMenu

class BluetoothListActivity : BaseStringListActivity() {

    override val optionsMenu: ArrayList<OptionsMenu>
        get() = arrayListOf(
            OptionsMenu("BLE GitHub", "https://github.com/Alex-Jerry/Android-BLE"),
            OptionsMenu(
                "BLE 详解",
                "https://mp.weixin.qq.com/s?__biz=MzIxNzU1Nzk3OQ==&mid=2247484750&idx=1&sn=b857c650ada19b00880d9b006296e403&chksm=97f6bbfaa08132ec3b58ab1818de3b33c63ed0787428733f17554d2e671c6175ae09565ff70b&scene=38#wechat_redirect"
            ),
            OptionsMenu(
                "Google 经典蓝牙",
                "https://developer.android.com/guide/topics/connectivity/bluetooth.html?hl=zh-cn#TheBasics"
            ),
            OptionsMenu(
                "Google BLE",
                "https://developer.android.com/guide/topics/connectivity/bluetooth-le.html?hl=zh-cn"
            )
        )

    override fun getItems(): MutableList<String> {
        return getStringArray(R.array.bluetooth_list)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (position) {
            0 -> {
                startActivity(Intent(this, BluetoothActivity::class.java).apply {
                    putExtra("type", "Classic Bluetooth")
                })
            }
            1 -> {
                startActivity(Intent(this, BluetoothActivity::class.java).apply {
                    putExtra("type", "Bluetooth Low Energy")
                })
            }
        }
    }
}
