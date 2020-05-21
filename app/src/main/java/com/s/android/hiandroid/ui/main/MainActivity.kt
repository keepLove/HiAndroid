package com.s.android.hiandroid.ui.main

import android.os.Bundle
import com.s.android.hiandroid.R
import com.s.android.hiandroid.common.utils.logD
import com.s.android.hiandroid.common.utils.showToast
import com.s.android.hiandroid.ui.android.AndroidActivity
import com.s.android.hiandroid.ui.common.BaseStringListActivity
import com.s.android.hiandroid.ui.common.info.StringListInfo
import com.s.android.hiandroid.ui.common.info.getStringListInfo
import com.s.android.hiandroid.ui.java.JavaActivity
import com.s.android.hiandroid.ui.kotlin.KotlinActivity
import com.s.android.hiandroid.ui.patterns.PatternsActivity

class MainActivity : BaseStringListActivity() {

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        supportActionBar?.setTitle(R.string.app_name)
        supportActionBar?.setHomeButtonEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        showToast("test33333")
        logD("test2")
    }

    override fun getItems(): MutableList<StringListInfo> {
        return mutableListOf(
            getStringListInfo("patterns", PatternsActivity::class.java),
            getStringListInfo("java", JavaActivity::class.java),
            getStringListInfo("android", AndroidActivity::class.java),
            getStringListInfo("kotlin", KotlinActivity::class.java)
        )
    }
}
