package com.s.android.hiandroid.ui.android

import android.content.Intent
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.s.android.hiandroid.R
import com.s.android.hiandroid.common.utils.getStringArray
import com.s.android.hiandroid.ui.android.bluetooth.BluetoothListActivity
import com.s.android.hiandroid.ui.android.bus.BusActivity
import com.s.android.hiandroid.ui.android.vpn.VPNActivity
import com.s.android.hiandroid.ui.android.wifi.WiFiActivity
import com.s.android.hiandroid.ui.common.BaseStringListActivity

class AndroidActivity : BaseStringListActivity() {

    override fun getItems(): MutableList<String> {
        return getStringArray(R.array.android_list)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (position) {
            0 -> {
                startActivity(Intent(this, BusActivity::class.java))
            }
            1 -> {
                startActivity(Intent(this, BluetoothListActivity::class.java))
            }
            2 -> {
                startActivity(Intent(this, VPNActivity::class.java))
            }
            3 -> {
                startActivity(Intent(this, WiFiActivity::class.java))
            }
        }
    }

}
