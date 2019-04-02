package com.s.android.hiandroid.ui.common

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.s.android.hiandroid.R
import kotlinx.android.synthetic.main.activity_practice.*

class PracticeActivity : BaseActivity() {

    override fun getLayoutResID(): Int? {
        return R.layout.activity_practice
    }

    override fun init(savedInstanceState: Bundle?) {
        supportActionBar?.title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        if (content.isNullOrEmpty()) {
            tv_content_title.visibility = View.GONE
            tv_content.visibility = View.GONE
        } else {
            tv_content.text = intent.getStringExtra("content")
        }
        val practice = intent.getStringExtra("practice")
        if (practice.isNullOrEmpty()) {
            tv_practice_title.visibility = View.GONE
            tv_practice.visibility = View.GONE
        } else {
            tv_practice.text = intent.getStringExtra("practice")
        }
    }

}

fun Context.startPracticeActivity(title: String?, content: String = "", practice: String = "") {
    startActivity(Intent(this, PracticeActivity::class.java).apply {
        putExtra("title", title)
        putExtra("content", content)
        putExtra("practice", practice)
    })
}
