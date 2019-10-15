package com.s.android.hiandroid.ui.android.other

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.s.android.hiandroid.R
import com.s.android.hiandroid.ui.common.BaseActivity
import kotlinx.android.synthetic.main.activity_ripple.*

/**
 * 水波纹效果
 */
class RippleActivity : BaseActivity() {

    override fun getLayoutResID(): Int? {
        return R.layout.activity_ripple
    }

    override fun init(savedInstanceState: Bundle?) {
        val handle = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message?) {
                Log.e("test", "handleMessage: ${msg?.what}")
//                testView.text = msg?.what?.toString() ?: "unknow"
                testView.requestLayout()
            }
        }
        button.setOnClickListener {
            repeat(20) {
                handle.sendEmptyMessage(it)
//                testView.requestLayout()
            }
            Log.e("test", "repeat end.")
        }
    }
}
