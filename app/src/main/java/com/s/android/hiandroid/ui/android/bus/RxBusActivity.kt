package com.s.android.hiandroid.ui.android.bus

import android.annotation.SuppressLint
import android.os.Bundle
import com.s.android.hiandroid.R
import com.s.android.hiandroid.common.bus.RxBus
import com.s.android.hiandroid.common.utils.showToastAndLog
import com.s.android.hiandroid.ui.common.BaseActivity
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_rx_bus.*
import kotlin.concurrent.thread

class RxBusActivity : BaseActivity() {

    override fun getLayoutResID(): Int? {
        return R.layout.activity_rx_bus
    }

    @SuppressLint("CheckResult", "SetTextI18n")
    override fun init(savedInstanceState: Bundle?) {
        RxBus.instance.toObservable(RxBusEvent4::class.java)
                .bindToLifecycle(this)
                .subscribe({
                    showToastAndLog("消息来了:$it")
                }, {
                    it.printStackTrace()
                })
        RxBus.instance.toObservable(RxBusEvent1::class.java)
                .subscribe({
                    showToastAndLog("消息来了:$it")
                }, {
                    it.printStackTrace()
                })
        RxBus.instance.toObservable(RxBusEvent2::class.java)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    showToastAndLog("消息来了:$it")
                }, {
                    it.printStackTrace()
                })
        bt_send.setOnClickListener {
            RxBus.instance.postValue(RxBusEvent1("普通消息"))
        }
        bt_send_async.setOnClickListener {
            thread {
                RxBus.instance.postValue(RxBusEvent2("异步消息"))
            }
        }
        bt_send_other.setOnClickListener {
            RxBus.instance.postValue(RxBusEvent3("其它页面的消息"))
        }
        tv_practice.text = """
class RxBus private constructor() {

    private val relay: Relay<Any> = PublishRelay.create<Any>().toSerialized()

    fun postValue(any: Any) {
        relay.accept(any)
    }

    fun <T> toObservable(clazz: Class<T>): Observable<T> {
        return relay.ofType(clazz)
    }

    companion object {
        val instance: RxBus by lazy { RxBus() }
    }
}
        """.trimIndent()
    }
}
