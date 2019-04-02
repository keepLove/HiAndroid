package com.s.android.hiandroid.ui.java

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.s.android.hiandroid.R
import com.s.android.hiandroid.common.utils.getStringArray
import com.s.android.hiandroid.ui.common.BaseStringListActivity

class JavaActivity : BaseStringListActivity() {

    override fun getItems(): MutableList<String> {
        return getStringArray(R.array.java_list)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (position) {
            0 -> {
            }
            else -> {
            }
        }
    }

}
