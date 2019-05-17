package com.s.android.hiandroid.ui.android.aidl

import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import com.s.android.hiandroid.common.utils.logD

class RemoteService : Service() {

    private val mList = mutableListOf<Offer>()
    private val mListenerList = RemoteCallbackList<IOnNewOfferArrivedInterface>()
    private val mBinder = object : JobsInterface.Stub() {

        override fun basicTypes(
            anInt: Int,
            aLong: Long,
            aBoolean: Boolean,
            aFloat: Float,
            aDouble: Double,
            aString: String?
        ) {
            logD("basicTypes")
        }

        override fun queryOffers(): MutableList<Offer> {
            return mList
        }

        override fun addOffer(offer: Offer) {
            mList.add(offer)
            // 向客户端通信
            val size = mListenerList.beginBroadcast()
            for (i in 0 until size) {
                val listener = mListenerList.getBroadcastItem(i)
                listener.onNewOfferArrived(offer)
            }
            mListenerList.finishBroadcast()
        }

        override fun registerListener(listener: IOnNewOfferArrivedInterface?) {
            mListenerList.register(listener)
        }

        override fun unregisterListener(listener: IOnNewOfferArrivedInterface?) {
            mListenerList.unregister(listener)
        }

        override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
            // 验证权限 返回false代表权限未验证通过
            val check = checkCallingOrSelfPermission("com.s.android.hiandroid.permission.ACCESS_OFFER_SERVICE")
            if (check == PackageManager.PERMISSION_DENIED) {
                return false
            }
            val packages = packageManager.getPackagesForUid(Binder.getCallingUid())
            if (packages != null && packages.isNotEmpty()) {
                if (!packages[0].endsWith("hiandroid")) {
                    return false
                }
            }
            return super.onTransact(code, data, reply, flags)
        }
    }

    override fun onCreate() {
        super.onCreate()
        mList.add(Offer(5000, "智联招聘"))
    }

    override fun onBind(intent: Intent): IBinder {
        Handler().postDelayed({
            mBinder.addOffer(Offer(4500, "51job"))
        }, 1000)
        return mBinder
    }
}