package com.s.android.hiandroid.ui.android.bus

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
//import com.jeremyliao.liveeventbus.LiveEventBus
import com.s.android.hiandroid.common.bus.LiveDataBus
import com.s.android.hiandroid.common.bus.RxBus
import com.s.android.hiandroid.common.utils.getStringArray
import com.s.android.hiandroid.common.utils.showToastAndLog
import com.s.android.hiandroid.ui.common.BaseStringListActivity
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.android.schedulers.AndroidSchedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class BusActivity : BaseStringListActivity() {

    @SuppressLint("CheckResult")
    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        LiveDataBus.with<String>("button3").observe(this, Observer {
            showToastAndLog("消息来了：$it")
        })
        LiveDataBus.with<String>("start_send_message")
            .observe(this, Observer {
                showToastAndLog("这个可以有")
            })
        RxBus.instance.toObservable(RxBusEvent3::class.java)
            .bindToLifecycle(this)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                showToastAndLog("消息来了:$it")
            }, {
                it.printStackTrace()
            })
    }

    override fun getItems(): MutableList<String> {
        return getStringArray(com.s.android.hiandroid.R.array.bus_list)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (position) {
            0 -> {
//                LiveEventBus.get().with("key_name").setValue("00000000")
                LiveDataBus.with<String>("start_send_message").setValue("开始就发送消息，试试未打开的页面能否接收到消息，出现了就说明有问题.")
                LiveDataBus.with<String>("start_send_message_forever")
                    .setValue("forever:开始就发送消息，试试未打开的页面能否接收到消息，出现了就说明有问题.")
                startActivity(Intent(this, LiveDataBusActivity::class.java))
            }
            1 -> {
                RxBus.instance.postValue(RxBusEvent4("开始就发送消息，试试未打开的页面能否接收到消息，出现了就说明有问题."))
                startActivity(Intent(this, RxBusActivity::class.java))
            }
            2 -> {
                EventBus.getDefault().post(EventMessage2("开始就发送消息，试试未打开的页面能否接收到消息，出现了就说明有问题."))
                startActivity(Intent(this, EventBusActivity::class.java))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    public override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent1(event: EventMessage1) {
        showToastAndLog("$event")
    }

}
