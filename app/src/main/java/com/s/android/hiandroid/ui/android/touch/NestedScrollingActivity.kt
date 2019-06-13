package com.s.android.hiandroid.ui.android.touch

import android.os.Bundle
import com.s.android.hiandroid.R
import com.s.android.hiandroid.ui.common.BaseActivity
import com.s.android.hiandroid.ui.common.info.OptionsMenu

class NestedScrollingActivity : BaseActivity() {

    override val optionsMenu: ArrayList<OptionsMenu>
        get() = arrayListOf(
            OptionsMenu(
                "触摸事件分发机制",
                "https://mp.weixin.qq.com/s?__biz=MzA5MzI3NjE2MA==&mid=2650243901&idx=1&sn=ed25edbea7331ff7948874e055440ac1&chksm=88637252bf14fb4493f1965e6108e4a96c2ed1d713b7cd438dd6349e68b756249ad2677f2787&scene=38#wechat_redirect"
            ),
            OptionsMenu("NestedScrolling", "https://blog.csdn.net/recall2012/article/details/79474172")
        )

    override fun getLayoutResID(): Int? {
        return R.layout.activity_nested_scrolling
    }

    override fun init(savedInstanceState: Bundle?) {
    }
}
