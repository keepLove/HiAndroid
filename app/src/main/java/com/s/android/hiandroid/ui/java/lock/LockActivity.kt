package com.s.android.hiandroid.ui.java.lock

import android.annotation.SuppressLint
import android.os.Bundle
import com.s.android.hiandroid.R
import com.s.android.hiandroid.ui.common.BaseActivity
import kotlinx.android.synthetic.main.activity_lock.*

/**
 * https://blog.csdn.net/mmoren/article/details/79185862
 * https://mp.weixin.qq.com/s/JPM5KxqvjQ1uEzOwlA47yA
 * https://mp.weixin.qq.com/s?__biz=MzIxNjc0ODExMA==&mid=2247483986&idx=7&sn=6b60d40ad342fa33aed971da0fc44b74&chksm=97851b73a0f29265b417085000d26bae050be00356c0d6aa836c10c5ecb46a694f0cfea73748&scene=38#wechat_redirect
 */
class LockActivity : BaseActivity() {

    override fun getLayoutResID(): Int? {
        return R.layout.activity_lock
    }

    @SuppressLint("SetTextI18n")
    override fun init(savedInstanceState: Bundle?) {
        tv_synchronized.text = """
    同步锁使用场景：多个线程对同一个对象中的实例变量进行并发访问。

    public synchronized void printNum(String name){}

    使用对象锁分为：synchronized(this)锁，synchronized(非this对象)锁。synchronized(this)锁与synchronized关键字在方法声明是一样的作用，优点都是解决多线程同步问题。synchronized(非this对象)，对比与synchronized(this)的优点：提高多个方法同步的效率问题。

    public synchronized void threadMethodA() throws InterruptedException {}

    public void threadMethodB() throws InterruptedException {
        synchronized (this) {}
    }

    静态锁：应用在static静态方法上，锁为当前*.java文件的Class类。

    public static synchronized void methodA() {
    }

    死锁:出现死锁的情形：两个或多个线程处于永久等待状态，每个线程都等待其他线程释放所持有的资源（锁）。

公平锁/非公平锁（多线程执行顺序的维度）

    公平锁：加锁前先查看是否有排队等待的线程，有的话优先处理排在前面的线程，先来先得。

    非公平所：线程加锁时直接尝试获取锁，获取不到就自动到队尾等待。

    //创建一个非公平锁，默认是非公平锁

Lock nonFairLock= new ReentrantLock();

Lock nonFairLock= new ReentrantLock(false);

//创建一个公平锁，构造传参true

Lock fairLock= new ReentrantLock(true);

更多的是直接使用非公平锁：非公平锁比公平锁性能高5-10倍，因为公平锁需要在多核情况下维护一个队列，如果当前线程不是队列的第一个无法获取锁，增加了线程切换次数。

乐观锁/悲观锁（多线程操作共享数据的维度）

悲观锁：假设一定会发生并发冲突，通过阻塞其他所有线程来保证数据的完整性。

乐观锁：假设不会发生并发冲突，直接不加锁去完成某项更新，如果冲突就返回失败。

悲观锁：Synchronized多线程同步，具有排他性，也会容易产生死锁。

乐观锁：CAS机制，简单来说会有三个操作数，当前内存变量值V，变量预期值A，即将更新值B，当需要更新变量的时候，会直接将变量值V和预期值A进行比较，如果相同，则直接更新为B；如果不相同，则当前变量值V刷新到预期值中，然后重新尝试比较更新。

乐观锁：适用于数据争用不严重/重试代价不大/需要相应速度快的场景。

悲观锁：适用于数据争用严重/重试代价大的场景。

            """.trimIndent()
    }
}
