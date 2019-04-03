package com.s.android.hiandroid.ui.android.bus

import android.os.Bundle
import com.s.android.hiandroid.R
import com.s.android.hiandroid.common.utils.showToastAndLog
import com.s.android.hiandroid.ui.common.BaseActivity
import kotlinx.android.synthetic.main.activity_event_bus.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.concurrent.thread


class EventBusActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    public override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun getLayoutResID(): Int? {
        return R.layout.activity_event_bus
    }

    override fun init(savedInstanceState: Bundle?) {
        bt_send.setOnClickListener {
            EventBus.getDefault().post(EventMessage3("普通消息发送"))
        }
        bt_send_async.setOnClickListener {
            thread {
                EventBus.getDefault().post(EventMessage4("异步消息发送"))
            }
        }
        bt_send_other.setOnClickListener {
            EventBus.getDefault().post(EventMessage1("来自其它星球的生物."))
        }
    }

    @Subscribe
    fun onMessageEvent(event: EventMessage2) {
        showToastAndLog("$event")
    }

    @Subscribe
    fun onMessageEvent(event: EventMessage3) {
        showToastAndLog("$event")
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: EventMessage4) {
        showToastAndLog("$event")
    }
}
