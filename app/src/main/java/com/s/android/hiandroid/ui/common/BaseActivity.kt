package com.s.android.hiandroid.ui.common

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.layout_tool_bar.*

/**
 * Created by Administrator on 2019/3/18.
 */
abstract class BaseActivity : RxAppCompatActivity() {

    open val optionsMenu = arrayListOf<OptionsMenu>()

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
            // 返回键导航的显示与隐藏
            setDisplayHomeAsUpEnabled(true)
        }
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        optionsMenu.forEachIndexed { index, optionsMenu ->
            menu.add(0, index, index, optionsMenu.title)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (optionsMenu.size > item.itemId) {
            val menu = optionsMenu[item.itemId]
            startWebActivity(menu.url, menu.title)
        }
        return super.onOptionsItemSelected(item)
    }

    abstract fun getLayoutResID(): Int?

    abstract fun init(savedInstanceState: Bundle?)
}