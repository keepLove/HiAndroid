package com.s.android.hiandroid.ui.kotlin

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.s.android.hiandroid.R
import com.s.android.hiandroid.common.utils.getStringArray
import com.s.android.hiandroid.ui.common.BaseStringListActivity
import com.s.android.hiandroid.ui.common.startPracticeActivity
import com.s.android.hiandroid.ui.kotlin.singleton.singleton_practice

class KotlinActivity : BaseStringListActivity() {

    override fun getItems(): MutableList<String> {
        return getStringArray(R.array.kotlin_list)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (position) {
            0 -> {
                startPracticeActivity(stringListAdapter.getItem(position), practice = singleton_practice)
            }
            else -> {
            }
        }
    }

}
