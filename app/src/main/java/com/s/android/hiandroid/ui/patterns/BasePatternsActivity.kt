package com.s.android.hiandroid.ui.patterns

import android.os.Bundle
import com.s.android.hiandroid.R
import com.s.android.hiandroid.ui.common.BaseActivity
import kotlinx.android.synthetic.main.activity_pattern.*

class BasePatternsActivity : BaseActivity() {

    override fun getLayoutResID(): Int? {
        return R.layout.activity_pattern
    }

    override fun init(savedInstanceState: Bundle?) {
        supportActionBar?.title = intent.getStringExtra("title")
        tv_content.text = intent.getStringExtra("content")
        tv_practice.text = intent.getStringExtra("practice")
    }

}