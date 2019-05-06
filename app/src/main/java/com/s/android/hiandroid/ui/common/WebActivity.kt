package com.s.android.hiandroid.ui.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_web.*


class WebActivity : BaseActivity() {

    override fun getLayoutResID(): Int? {
        return com.s.android.hiandroid.R.layout.activity_web
    }

    override fun init(savedInstanceState: Bundle?) {
        web_view.loadUrl(intent.getStringExtra("url"))
        intent.getStringExtra("title")?.let {
            supportActionBar?.title = it
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add("在浏览器打开")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        startBrowser(web_view.originalUrl)
        return super.onOptionsItemSelected(item)
    }

}

fun Context.startWebActivity(url: String, title: String? = null) {
    val intent = Intent(this, WebActivity::class.java)
    intent.putExtra("url", url)
    intent.putExtra("title", title)
    startActivity(intent)
}

fun Context.startBrowser(url: String) {
    val intent = Intent()
    intent.action = "android.intent.action.VIEW"
    val uri = Uri.parse(url)
    intent.data = uri
    startActivity(intent)
}
