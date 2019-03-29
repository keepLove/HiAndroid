package com.s.android.hiandroid.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle2.components.support.RxFragment

/**
 * Created by Administrator on 2019/3/18.
 */
abstract class BaseFragment : RxFragment() {

    var mView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (mView == null) {
            mView = inflater.inflate(getLayoutResID(), container)
            init(savedInstanceState)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    abstract fun getLayoutResID(): Int

    abstract fun init(savedInstanceState: Bundle?)
}