package com.s.android.hiandroid.ui.common

import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.s.android.hiandroid.R
import com.s.android.hiandroid.common.BaseRecyclerAdapter
import com.s.android.hiandroid.ui.common.info.StringListInfo
import kotlinx.android.synthetic.main.layout_item_list.*

abstract class BaseStringListActivity : BaseActivity(), BaseQuickAdapter.OnItemClickListener {

    lateinit var stringListAdapter: BaseRecyclerAdapter<StringListInfo>

    @CallSuper
    override fun init(savedInstanceState: Bundle?) {
        initRecycler(recycler_view)
    }

    override fun getLayoutResID(): Int? {
        return R.layout.layout_item_list
    }

    private fun initRecycler(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))
        stringListAdapter = object : BaseRecyclerAdapter<StringListInfo>(R.layout.item_main, getItems()) {
            override fun convert(helper: BaseViewHolder, item: StringListInfo) {
                helper.setText(R.id.text_view, item.title)
            }
        }
        stringListAdapter.onItemClickListener = this
        recyclerView.adapter = stringListAdapter
    }

    abstract fun getItems(): MutableList<StringListInfo>

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        stringListAdapter.getItem(position)?.let {
            if (it.targetClass == null) {
                startWebActivity(it.url ?: "https://www.baidu.com", it.title)
            } else {
                startActivity(Intent(this, it.targetClass))
            }
        }
    }
}