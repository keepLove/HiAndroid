package com.s.android.hiandroid.ui.common

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.s.android.hiandroid.R
import com.s.android.hiandroid.common.BaseRecyclerAdapter
import kotlinx.android.synthetic.main.layout_item_list.*

abstract class BaseStringListActivity : BaseActivity(), BaseQuickAdapter.OnItemClickListener {

    lateinit var stringListAdapter: BaseRecyclerAdapter<String>

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
        stringListAdapter = object : BaseRecyclerAdapter<String>(R.layout.item_main, getItems()) {
            override fun convert(helper: BaseViewHolder, item: String) {
                helper.setText(R.id.text_view, item)
            }
        }
        stringListAdapter.onItemClickListener = this
        recyclerView.adapter = stringListAdapter
    }

    abstract fun getItems(): MutableList<String>

}