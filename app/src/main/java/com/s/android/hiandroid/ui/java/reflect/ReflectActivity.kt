package com.s.android.hiandroid.ui.java.reflect

import android.annotation.SuppressLint
import android.os.Bundle
import com.s.android.hiandroid.R
import com.s.android.hiandroid.ui.common.BaseActivity
import kotlinx.android.synthetic.main.activity_reflect.*

class ReflectActivity : BaseActivity() {

    override fun getLayoutResID(): Int? {
        return R.layout.activity_reflect
    }

    @SuppressLint("SetTextI18n")
    override fun init(savedInstanceState: Bundle?) {
        tv_content.text = """
详细信息：https://mp.weixin.qq.com/s?__biz=MzAxMTI4MTkwNQ==&mid=2650825931&idx=1&sn=b61ed2ad2665cbc3a89351b161223769&chksm=80b7b055b7c03943f10f1e8b3697d3d33bbfc98d38e40c9ecbc6a3ab34f700a065858f8c8ce9&scene=38#wechat_redirect


        """.trimIndent()
    }
}
