package com.s.android.hiandroid.ui.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_web.*


class WebActivity : BaseActivity() {

    override fun getLayoutResID(): Int? {
        return com.s.android.hiandroid.R.layout.activity_web
    }

    override fun init(savedInstanceState: Bundle?) {
        web_view.loadUrl(intent.getStringExtra("url"))
    }

}

fun Context.startWebActivity(url: String) {
    val intent = Intent(this, WebActivity::class.java)
    intent.putExtra("url", url)
    startActivity(intent)
}

fun Context.startBrowser(url: String) {
    val intent = Intent()
    intent.action = "android.intent.action.VIEW"
    val uri = Uri.parse(url)
    intent.data = uri
    startActivity(intent)
}