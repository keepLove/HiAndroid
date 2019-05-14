package com.s.android.hiandroid.ui.kotlin

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.s.android.hiandroid.ui.common.BaseStringListActivity
import com.s.android.hiandroid.ui.common.PracticeActivity
import com.s.android.hiandroid.ui.common.info.StringListInfo
import com.s.android.hiandroid.ui.common.info.getStringListInfo
import com.s.android.hiandroid.ui.common.startPracticeActivity
import com.s.android.hiandroid.ui.kotlin.singleton.singleton_practice

class KotlinActivity : BaseStringListActivity() {

    override fun getItems(): MutableList<StringListInfo> {
        return mutableListOf(
            getStringListInfo("kotlin实现单例的5种方式", PracticeActivity::class.java)
        )
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (position) {
            0 -> {
                startPracticeActivity(stringListAdapter.getItem(position)?.title, practice = singleton_practice)
            }
            else -> {
            }
        }
    }

}
