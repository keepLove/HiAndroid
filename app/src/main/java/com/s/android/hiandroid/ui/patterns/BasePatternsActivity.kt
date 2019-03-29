package com.s.android.hiandroid.ui.patterns

import android.os.Bundle
import com.s.android.hiandroid.R
import com.s.android.hiandroid.ui.common.BaseActivity
import kotlinx.android.synthetic.main.layout_pattern.*

class BasePatternsActivity : BaseActivity() {

    override fun getLayoutResID(): Int? {
        return R.layout.layout_pattern
    }

    override fun init(savedInstanceState: Bundle?) {
        supportActionBar?.title = intent.getStringExtra("title")
        tv_content.text = intent.getStringExtra("content")
        tv_practice.setText(intent.getStringExtra("practice"))
    }

}