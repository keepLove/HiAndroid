package com.s.android.hiandroid.common.bus

import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable

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