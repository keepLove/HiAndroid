package com.s.android.hiandroid.ui.common

import android.os.Bundle
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.layout_tool_bar.*

/**
 * Created by Administrator on 2019/3/18.
 */
abstract class BaseActivity : RxAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getLayoutResID()?.let {
            setContentView(it)
            initToolBar()
        }
        init(savedInstanceState)
    }

    private fun initToolBar() {
        toolbar.title = javaClass.simpleName.replace("Activity", "")
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            // 设置返回键可用
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    abstract fun getLayoutResID(): Int?

    abstract fun init(savedInstanceState: Bundle?)
}