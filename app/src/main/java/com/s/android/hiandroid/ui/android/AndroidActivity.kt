package com.s.android.hiandroid.ui.android

import android.content.Intent
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.s.android.hiandroid.R
import com.s.android.hiandroid.common.utils.getStringArray
import com.s.android.hiandroid.ui.android.bus.BusActivity
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
            else -> {
            }
        }
    }

}
