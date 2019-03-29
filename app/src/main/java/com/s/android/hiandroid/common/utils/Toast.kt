package com.s.android.hiandroid.common.utils

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.s.android.hiandroid.App
import com.s.android.hiandroid.R

/**
 * Created by Administrator on 2019/3/8.
 */
class ToastUtils {

    companion object {

        private var toast: Toast? = null

        @JvmStatic
        fun showToast(message: String?, duration: Int = Toast.LENGTH_SHORT, drawable: Int = 0) {
            if (toast == null) {
                val customToast = Toast(App.app)
                customToast.view = LayoutInflater.from(App.app).inflate(R.layout.layout_toast, null)
                customToast.setGravity(Gravity.CENTER, 0, 0)
                toast = customToast
            }
            if (message.isNullOrEmpty()) return
            val textView: TextView? = toast?.view?.findViewById(R.id.tv_toast)
            val imageView: ImageView? = toast?.view?.findViewById(R.id.iv_toast)
            if (drawable != 0) {
                imageView?.visibility = View.VISIBLE
                imageView?.setImageResource(drawable)
            } else {
                imageView?.visibility = View.GONE
            }
            textView?.text = message
            toast?.duration = duration
            toast?.show()
        }
    }
}

/**
 * 显示toast
 */
fun showToast(message: String?, duration: Int = Toast.LENGTH_SHORT, drawable: Int = 0) {
    ToastUtils.showToast(message, duration, drawable)
}
