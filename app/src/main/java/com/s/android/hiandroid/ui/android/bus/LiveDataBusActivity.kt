package com.s.android.hiandroid.ui.android.bus

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.os.Bundle
import com.jeremyliao.liveeventbus.LiveEventBus
import com.s.android.hiandroid.common.bus.LiveDataBus
import com.s.android.hiandroid.common.utils.showToast
import com.s.android.hiandroid.common.utils.showToastAndLog
import com.s.android.hiandroid.ui.common.BaseActivity
import kotlinx.android.synthetic.main.activity_live_data_bus.*
import kotlin.concurrent.thread


class LiveDataBusActivity : BaseActivity() {

    override fun getLayoutResID(): Int? {
        return com.s.android.hiandroid.R.layout.activity_live_data_bus
    }

    override fun onResume() {
        super.onResume()
        LiveEventBus.get()
                .with("key_name", String::class.java)
                .observe(this, Observer {
                    showToastAndLog("消息来了：$it")
                })
        LiveDataBus.with<String>("start_send_message")
                .observe(this, Observer {
                    showToastAndLog("消息来了：$it")
                })
        LiveDataBus.with<String>("start_send_message_forever")
                .observe(this, Observer {
                    showToastAndLog("消息来了：$it")
                })
    }

    @SuppressLint("SetTextI18n")
    override fun init(savedInstanceState: Bundle?) {
        LiveDataBus.with<String>("start_send_message")
                .observe(this, Observer {
                    showToastAndLog("消息来了：$it")
                })
        LiveDataBus.with<String>("button1")
                .observe(this, Observer {
                    showToastAndLog("消息来了：$it")
                })
        bt_send.setOnClickListener {
            LiveDataBus.with<String>("button1").setValue("普通消息发送")
        }
        LiveDataBus.with<String>("button2")
                .observe(this, Observer {
                    showToastAndLog("消息来了：$it")
                })
        bt_send_async.setOnClickListener {
            thread {
                LiveDataBus.with<String>("button2").postValue("异步消息发送")
            }
        }
        bt_send_other.setOnClickListener {
            showToast("返回页面试一下！！！")
            LiveDataBus.with<String>("button3").postValue("返回页面消息发送")
        }
        tv_practice.text = """
// 更成熟的LiveEventBus：https://github.com/JeremyLiao/LiveEventBus
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
        """.trimIndent()
    }

}
