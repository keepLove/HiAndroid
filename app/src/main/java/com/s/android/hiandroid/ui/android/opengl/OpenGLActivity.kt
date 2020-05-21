package com.s.android.hiandroid.ui.android.opengl

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import com.s.android.hiandroid.R
import com.s.android.hiandroid.ui.common.BaseActivity
import com.s.android.hiandroid.ui.common.PageFragment
import com.s.android.hiandroid.ui.common.info.OptionsMenu
import com.s.android.hiandroid.ui.common.info.PageModel
import kotlinx.android.synthetic.main.activity_custom_view.*

class OpenGLActivity : BaseActivity() {

    override val optionsMenu: ArrayList<OptionsMenu>
        get() = arrayListOf(
            OptionsMenu("GL-ES", "https://juejin.im/post/5c382b926fb9a049f23cf8cc")
        )

    private val pageModels = listOf(
        PageModel("基础绘制", R.layout.layout_open_gl_test1)
    )

    override fun init(savedInstanceState: Bundle?) {
        pager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {

            override fun getItem(position: Int): Fragment {
                val pageModel = pageModels[position]
                return PageFragment.newInstance(pageModel.layoutRes)
            }

            override fun getCount(): Int {
                return pageModels.size
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return pageModels[position].title
            }
        }

        tabLayout.setupWithViewPager(pager)
    }

    override fun getLayoutResID(): Int? {
        return R.layout.activity_open_gl
    }

}
