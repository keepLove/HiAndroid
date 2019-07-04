package com.s.android.hiandroid.ui.android.customview

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import com.s.android.hiandroid.R
import com.s.android.hiandroid.ui.common.BaseActivity
import com.s.android.hiandroid.ui.common.PageFragment
import com.s.android.hiandroid.ui.common.info.OptionsMenu
import com.s.android.hiandroid.ui.common.info.PageModel
import kotlinx.android.synthetic.main.activity_custom_view.*

class CustomViewActivity : BaseActivity() {

    override val optionsMenu: ArrayList<OptionsMenu>
        get() = arrayListOf(
            OptionsMenu("HenCoder", "https://hencoder.com/tag/hui-zhi/")
        )

    private val pageModels = listOf(
        PageModel("基础绘制", R.layout.layout_custom_view1),
        PageModel("饼状图", R.layout.layout_custom_view2),
        PageModel("动画", R.layout.layout_custom_view3),
        PageModel("仿Flipboard的折页效果", R.layout.layout_custom_view4),
        PageModel("仿即刻的点赞效果", R.layout.layout_custom_view5),
        PageModel("仿薄荷健康卡尺效果", R.layout.layout_custom_view6)
    )

    override fun getLayoutResID(): Int? {
        return R.layout.activity_custom_view
    }

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

}
