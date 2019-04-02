package com.s.android.hiandroid.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.s.android.hiandroid.R
import com.s.android.hiandroid.common.utils.getStringArray
import com.s.android.hiandroid.ui.android.AndroidActivity
import com.s.android.hiandroid.ui.common.BaseStringListActivity
import com.s.android.hiandroid.ui.java.JavaActivity
import com.s.android.hiandroid.ui.kotlin.KotlinActivity
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
            1 -> {
                startActivity(Intent(this, JavaActivity::class.java))
            }
            2 -> {
                startActivity(Intent(this, AndroidActivity::class.java))
            }
            3 -> {
                startActivity(Intent(this, KotlinActivity::class.java))
            }
            else -> {
            }
        }
    }
}
