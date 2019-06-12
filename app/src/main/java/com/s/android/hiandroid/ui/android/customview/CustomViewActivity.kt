package com.s.android.hiandroid.ui.android.customview

import android.os.Bundle
import com.s.android.hiandroid.R
import com.s.android.hiandroid.ui.common.BaseActivity
import com.s.android.hiandroid.ui.common.info.OptionsMenu

class CustomViewActivity : BaseActivity() {

    override val optionsMenu: ArrayList<OptionsMenu>
        get() = arrayListOf(
            OptionsMenu("HenCoder", "https://hencoder.com/tag/hui-zhi/")
        )

    override fun getLayoutResID(): Int? {
        return R.layout.activity_custom_view
    }

    override fun init(savedInstanceState: Bundle?) {
    }

}
