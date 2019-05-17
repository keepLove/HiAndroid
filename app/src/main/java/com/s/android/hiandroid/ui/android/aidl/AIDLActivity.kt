package com.s.android.hiandroid.ui.android.aidl

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import com.s.android.hiandroid.R
import com.s.android.hiandroid.common.utils.logD
import com.s.android.hiandroid.ui.common.BaseActivity
import com.s.android.hiandroid.ui.common.info.OptionsMenu
import kotlinx.android.synthetic.main.activity_aidl.*

class AIDLActivity : BaseActivity() {

    override val optionsMenu: ArrayList<OptionsMenu>
        get() = arrayListOf(
            OptionsMenu(
                "AIDL",
                "https://mp.weixin.qq.com/s/2AUugGBpqguzXNixOstLdQ"
            )
        )
    var manager: JobsInterface? = null
    private val mArrivedListener = object : IOnNewOfferArrivedInterface.Stub() {
        override fun onNewOfferArrived(offer: Offer?) {
            logD("ThreadId:${Thread.currentThread().id}    offer:$offer")
        }
    }
    private val mConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            manager = JobsInterface.Stub.asInterface(service)
            val list = manager?.queryOffers()
            logD("$list")
            manager?.registerListener(mArrivedListener)
            service?.linkToDeath({
                logD("linkToDeath")
                // Binder 连接死亡回调 此处需要重置 manager 并发起重连
                manager?.asBinder()?.unlinkToDeath({
                    // Binder 删除时回调
                    logD("unlinkToDeath")
                }, 0)
                manager = null
                bindRemoteService()
            }, 0)
        }
    }

    override fun getLayoutResID(): Int? {
        return R.layout.activity_aidl
    }

    override fun init(savedInstanceState: Bundle?) {
        bindRemoteService()
        bt_send.setOnClickListener {
            manager?.addOffer(Offer(System.currentTimeMillis().toInt(), "测试"))
        }
        bt_all.setOnClickListener {
            logD("${manager?.queryOffers()}")
        }
    }

    private fun bindRemoteService() {
        // 设置了自定义权限的组件开起时需要通过隐式的开启
//        bindService(Intent(this, RemoteService::class.java), mConnection, Context.BIND_AUTO_CREATE)
        bindService(
            Intent().setClassName(
                "com.s.android.hiandroid",
                "com.s.android.hiandroid.ui.android.aidl.RemoteService"
            ),
            mConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onDestroy() {
        manager?.let {
            if (it.asBinder().isBinderAlive) {
                it.unregisterListener(mArrivedListener)
            }
        }
        unbindService(mConnection)
        super.onDestroy()
    }
}