package com.s.android.hiandroid.ui.java.lock

import kotlin.concurrent.thread

/**
 * 同步锁使用场景：多个线程对同一个对象中的实例变量进行并发访问。
 * 方法体中声明的局部变量不需要同步处理。
 */
object SynchronizedTest {

    @JvmStatic
    fun main(args: Array<String>) {
//        testSynchronized()
        testSynchronizedObject()
    }

    /**
     * 使用对象锁分为：synchronized(this)锁，synchronized(非this对象)锁。synchronized(this)锁与synchronized关键字在方法
     * 声明是一样的作用，优点都是解决多线程同步问题。synchronized(非this对象)，对比与synchronized(this)的优点：提高多个
     * 方法同步的效率问题
     */
    fun testSynchronizedObject() {
        val `object` = ThreadSynchronizedObject()
        val thread_1 = object : Thread("thread_1") {
            override fun run() {
                try {
                    `object`.threadMethodA()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }
        }
        val thread_2 = object : Thread("thread_2") {
            override fun run() {
                try {
                    `object`.threadMethodB()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }
        }
        thread_1.start()
        thread_2.start()
        Thread.sleep(3000)
        val start_time =
            if (ThreadSynchronizedTimeUtils.mMethodAIntoTime - ThreadSynchronizedTimeUtils.mMethodBIntoTime > 0)
                ThreadSynchronizedTimeUtils.mMethodAIntoTime
            else
                ThreadSynchronizedTimeUtils.mMethodBIntoTime
        val end_time =
            if (ThreadSynchronizedTimeUtils.mMethodAOutTime - ThreadSynchronizedTimeUtils.mMethodBOutTime > 0)
                ThreadSynchronizedTimeUtils.mMethodAOutTime
            else
                ThreadSynchronizedTimeUtils.mMethodBOutTime
        println("总耗时:" + (end_time - start_time))
    }

    fun testSynchronized() {
        // 测试局部变量
//        val privateNum = PrintPrivateNum()
        // 测试全局变量
//        val privateNum = PrintPrivateNum1()
        // 测试同步锁
        val privateNum = PrintPrivateNum2()
        val thread1 = thread(name = "thread_1", start = false) {
            privateNum.printNum(Thread.currentThread().name)
        }
        val thread2 = thread(name = "thread_2", start = false) {
            privateNum.printNum(Thread.currentThread().name)
        }
        thread1.start()
        thread2.start()
    }

}

internal class PrintPrivateNum {
    fun printNum(name: String) {
        var num = 0 // 局部变量不需要同步锁
        if ("thread_1" == name) {
            num += 300
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        if ("thread_2" == name) {
            num -= 100
        }
        println(Thread.currentThread().name + ",Num:" + num)
    }
}

internal class PrintPrivateNum1 {

    var num = 0

    /**
     * 可能出现thread_2,Num:200   thread_1,Num:200
     */
    fun printNum(name: String) {
        if ("thread_1" == name) {
            num += 300
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        if ("thread_2" == name) {
            num -= 100
        }
        println(Thread.currentThread().name + ",Num:" + num)
    }
}

internal class PrintPrivateNum2 {

    var num = 0

    /**
     * 固定返回thread_1,Num:300   thread_2,Num:200
     *
     * 每次有且只有一个线程执行该方法的方法体。
     */
    @Synchronized
    fun printNum(name: String) {
        if ("thread_1" == name) {
            num += 300
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        if ("thread_2" == name) {
            num -= 100
        }
        println(Thread.currentThread().name + ",Num:" + num)
    }
}


internal class ThreadSynchronizedObject {

    @Synchronized
    @Throws(InterruptedException::class)
    fun threadMethodA() {
        ThreadSynchronizedTimeUtils.setMethodAIntoTime()
        println(Thread.currentThread().name + ",进入threadMethodA")
        Thread.sleep(1000) ///<模拟方法请求耗时
        println(Thread.currentThread().name + ",退出threadMethodA")
        ThreadSynchronizedTimeUtils.setMethodAOutTime()
    }

    @Throws(InterruptedException::class)
    fun threadMethodB() {
        synchronized(this) {
            ThreadSynchronizedTimeUtils.setMethodBIntoTime()
            println(Thread.currentThread().name + ",进入threadMethodB")
            Thread.sleep(1000)
            println(Thread.currentThread().name + ",退出threadMethodB")
            ThreadSynchronizedTimeUtils.setMethodBOutTime()
        }
    }
}

internal object ThreadSynchronizedTimeUtils {

    var mMethodAIntoTime: Long = 0
    var mMethodAOutTime: Long = 0
    var mMethodBIntoTime: Long = 0
    var mMethodBOutTime: Long = 0

    fun setMethodAIntoTime() {
        mMethodAIntoTime = System.currentTimeMillis()
    }

    fun setMethodAOutTime() {
        mMethodAOutTime = System.currentTimeMillis()
    }

    fun setMethodBIntoTime() {
        mMethodBIntoTime = System.currentTimeMillis()
    }

    fun setMethodBOutTime() {
        mMethodBOutTime = System.currentTimeMillis()
    }
}