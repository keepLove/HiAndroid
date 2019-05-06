package com.s.android.hiandroid.ui.java.lock

/**
 * 出现死锁的情形：两个或多个线程处于永久等待状态，每个线程都等待其他线程释放所持有的资源（锁）。
 */
object ThreadDealDemo {
    @JvmStatic
    fun main(args: Array<String>) {
        val dealService = DealService()
        val thread_1 = object : Thread("thread_1") {
            override fun run() {
                dealService.methodA()
            }
        }
        val thread_2 = object : Thread("thread_2") {
            override fun run() {
                dealService.methodB()
            }
        }
        thread_1.start()
        thread_2.start()
    }
}

internal class DealService {
    private val lock1 = Any()
    private val lock2 = Any()

    fun methodA() {
        println("" + Thread.currentThread().name + ",等待获取lock1")
        synchronized(lock1) {
            try {
                println("" + Thread.currentThread().name + ",持有lock1")
                Thread.sleep(2000)
                println("" + Thread.currentThread().name + ",等待获取lock2")
                synchronized(lock2) {
                    println("" + Thread.currentThread().name + ",持有lock2")
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        }
    }

    fun methodB() {
        println("" + Thread.currentThread().name + ",等待获取lock2")
        synchronized(lock2) {
            try {
                println("" + Thread.currentThread().name + ",持有lock2")
                Thread.sleep(2000)
                println("" + Thread.currentThread().name + ",等待获取lock1")
                synchronized(lock1) {
                    println("" + Thread.currentThread().name + ",持有lock1")
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        }
    }
}

