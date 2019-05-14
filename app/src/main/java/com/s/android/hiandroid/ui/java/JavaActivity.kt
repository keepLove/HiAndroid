package com.s.android.hiandroid.ui.java

import com.s.android.hiandroid.ui.common.BaseStringListActivity
import com.s.android.hiandroid.ui.common.info.StringListInfo
import com.s.android.hiandroid.ui.common.info.getStringListInfo
import com.s.android.hiandroid.ui.java.lock.LockActivity
import com.s.android.hiandroid.ui.java.reflect.ReflectActivity
import com.s.android.hiandroid.ui.java.thread.ThreadActivity

class JavaActivity : BaseStringListActivity() {

    override fun getItems(): MutableList<StringListInfo> {
        return mutableListOf(
            getStringListInfo("reflect(反射)", ReflectActivity::class.java),
            getStringListInfo("线程和线程池", ThreadActivity::class.java),
            getStringListInfo("lock(锁)", LockActivity::class.java),
            getStringListInfo("动态代理", "https://www.cnblogs.com/gonjan-blog/p/6685611.html"),
            getStringListInfo("枚举实现原理", "https://blog.csdn.net/mhmyqn/article/details/48087247")
        )
    }

}
