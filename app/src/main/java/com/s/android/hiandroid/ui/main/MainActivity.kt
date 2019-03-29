package com.s.android.hiandroid.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.s.android.hiandroid.R
import com.s.android.hiandroid.common.utils.getStringArray
import com.s.android.hiandroid.ui.common.BaseStringListActivity
import com.s.android.hiandroid.ui.patterns.PatternsActivity

class MainActivity : BaseStringListActivity() {

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        supportActionBar?.setTitle(R.string.app_name)
        supportActionBar?.setHomeButtonEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun getItems(): MutableList<String> {
        return getStringArray(R.array.main_list)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (position) {
            0 -> {
                startActivity(Intent(this, PatternsActivity::class.java))
            }
            else -> {
            }
        }
    }
}
