package com.s.android.hiandroid.common.bus

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.support.annotation.NonNull
import com.s.android.hiandroid.common.utils.logD
import java.util.Map


object LiveDataBus {

    private val liveDataBus = mutableMapOf<String, BusLiveData<Any>>()

    fun <T> with(key: String): BusLiveData<T> {
        if (!liveDataBus.containsKey(key)) {
            liveDataBus[key] = BusLiveData(key)
        }
        return liveDataBus[key] as BusLiveData<T>
    }

}

class BusLiveData<T>(private val key: String) : MutableLiveData<T>() {

    private var observerMap = mutableMapOf<Observer<T>, Boolean>()
    private var hook = true

    /**
     * 当移除liveData时移除bus里的值
     */
    override fun removeObserver(observer: Observer<T>) {
        observerMap.remove(observer)
        super.removeObserver(observer)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<T>) {
        this.observerMap[observer] = false
        hook = true
        setActiveCount()
        super.observe(owner, observer)
    }

    override fun observeForever(observer: Observer<T>) {
        this.observerMap[observer] = false
        hook = true
        setActiveCount()
        super.observeForever(observer)
    }

    override fun postValue(value: T) {
        super.postValue(value)
    }

    override fun setValue(value: T) {
        hook = false
        this.observerMap.forEach {
            this.observerMap[it.key] = true
        }
        logD(observerMap)
        super.setValue(value)
    }

    /**
     * observer在onResume时，会直接调用这个方法
     *
     * 防止发送未注册的消息
     */
    override fun onActive() {
        if (hook) {
            this.observerMap.forEach {
                if (!it.value) {
                    hook(it.key)
                }
            }
        }
    }

    private fun setActiveCount() {
        val classLiveData = LiveData::class.java
        // 设置 activity count , 设置多个observer时需要每次调用onActive方法
        val fieldActivityCount = classLiveData.getDeclaredField("mActiveCount")
        fieldActivityCount.isAccessible = true
        fieldActivityCount.set(this, 0)
    }

    @Throws(Exception::class)
    private fun hook(@NonNull observer: Observer<T>) {
        val classLiveData = LiveData::class.java
        // 设置 activity count , 设置多个observer时需要每次调用onActive方法
        val fieldActivityCount = classLiveData.getDeclaredField("mActiveCount")
        fieldActivityCount.isAccessible = true
        fieldActivityCount.set(this, 0)
        // 获取 LiveData 里面的 mVersion
        val fieldVersion = classLiveData.getDeclaredField("mVersion")
        fieldVersion.isAccessible = true
        val objectVersion = fieldVersion.get(this)
        // 获取wrapper 里面的 mLastVersion
        val fieldObservers = classLiveData.getDeclaredField("mObservers")
        fieldObservers.isAccessible = true
        val objectObservers = fieldObservers.get(this)
        val classObservers = objectObservers.javaClass
        val methodGet = classObservers.getDeclaredMethod("get", Any::class.java)
        methodGet.isAccessible = true
        val objectWrapperEntry = methodGet.invoke(objectObservers, observer)
        var objectWrapper: Any? = null
        if (objectWrapperEntry is Map.Entry<*, *>) {
            objectWrapper = objectWrapperEntry.value
        }
        if (objectWrapper == null) {
            throw NullPointerException("Wrapper can not be bull!")
        }
        val classObserverWrapper = objectWrapper.javaClass.superclass
        val fieldLastVersion = classObserverWrapper.getDeclaredField("mLastVersion")
        fieldLastVersion.isAccessible = true
        // 设置 mLastVersion
        fieldLastVersion.set(objectWrapper, objectVersion)
    }
}