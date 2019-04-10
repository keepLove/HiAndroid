package com.s.android.hiandroid.ui.android.bluetooth

import android.content.Intent
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.s.android.hiandroid.R
import com.s.android.hiandroid.common.utils.getStringArray
import com.s.android.hiandroid.ui.common.BaseStringListActivity

class BluetoothListActivity : BaseStringListActivity() {

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
