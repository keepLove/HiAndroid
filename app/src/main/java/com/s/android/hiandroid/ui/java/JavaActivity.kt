package com.s.android.hiandroid.ui.java

import android.content.Intent
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.s.android.hiandroid.R
import com.s.android.hiandroid.common.utils.getStringArray
import com.s.android.hiandroid.ui.common.BaseStringListActivity
import com.s.android.hiandroid.ui.java.lock.LockActivity
import com.s.android.hiandroid.ui.java.reflect.ReflectActivity
import com.s.android.hiandroid.ui.java.thread.ThreadActivity

class JavaActivity : BaseStringListActivity() {

    override fun getItems(): MutableList<String> {
        return getStringArray(R.array.java_list)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (position) {
            0 -> {
                startActivity(Intent(this, ReflectActivity::class.java))
            }
            1 -> {
                startActivity(Intent(this, ThreadActivity::class.java))
            }
            2 -> {
                startActivity(Intent(this, LockActivity::class.java))
            }
        }
    }

}
