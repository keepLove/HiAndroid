package com.s.android.hiandroid.common

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class BaseViewModel : ViewModel() {

    val testObserver: MutableLiveData<String> = MutableLiveData()
}