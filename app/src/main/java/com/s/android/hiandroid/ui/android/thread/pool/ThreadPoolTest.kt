package com.s.android.hiandroid.ui.android.thread.pool

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

/**
在Java中，使用线程来异步执行任务。Java线程的创建与销毁需要一定的开销，如果我们为每一个任务创建一个新线程来执行，
这些线程的创建和销毁将消耗大量的计算资源。针对这种情况，我们需要使用线程池来管理线程，带来的好处有3个：

① 降低资源消耗。通过重复利用已创建的线程降低线程创建和销毁造成的消耗。
② 提高响应速度。当任务到达时，任务可以不需要等到线程创建就能立即执行。
③ 提高线程的可管理性。线程是稀缺资源，不能无限制创建，否则不但会消耗资源，还会降低系统的稳定性，
而使用线程池可以进行统一分配、调优和监控。
 */
object ThreadPoolTest {

    fun testThreadPool() {
        /*
        1. 线程池判断核心线程池是否已经满了，如果没有，则创建线程执行任务，如果满了，进入步骤2。
        2. 线程池判断工作队列是否满了，如果没有，则将任务存储在队列中，等待线程调用执行，进入步骤3，如果满了，进入步骤4。
        3. 再次检查线程池是否正在运行，线程池判断当前是否有线程工作，如果没有，则创建线程执行任务。
        4. 线程池判断线程池是否满了，如果没有，则创建线程执行任务，如果满了，进入步骤4。
        5. 线程池判断线程池满了，按照策略处理无法执行的任务。
         */
        /*
        举个例子：
        假设某半成品加工工厂的车间有15个办公座位，工厂的仓库最多容纳30件半成品。工厂开业时只有1名员工，来了任务就处理，
        但第二个任务来了后，原有的1名员工仍在工作，处理不了，所以就再招聘了一名员工，就这样陆续招聘了10个在编员工。

        再来了一个任务后，就把任务放仓库，这10个员工中哪个空闲就会从仓库取半成品加工，突然有一天任务来的太快，仓库堆满
        了30件半成品，而这10名员工都在工作，考虑效率就招聘了一名临时员工，临时员工在工作，仓库又堆满了30件半成品，又招聘了
        一名临时员工，陆续招聘了5个临时员工。

        有一天仓库堆满了30件半成品，15个员工都在工作，仓库已满，车间办公座位已满，再有任务来就拒绝接收。

        那为什么要这样设计呢？是想尽可能地避免获取全局锁（严重的可伸缩瓶颈：每次创建线程都需要获取全局锁）——在当前运行
        的线程数大于等于corePoolSize以后，几乎所有的execute方法调会将任务放入阻塞队列，然后由线程处理队列中的任务，而任务放
        入阻塞队列并不需要获取全局锁。
         */
        val cachePool = Executors.newCachedThreadPool()
        // execute方法用于提交不需要返回值的任务，所以无法判断任务是否被线程池执行成功；
        cachePool.execute(TestRunnable())
        /*
        submit方法用于提交需要返回值的任务。线程池会返回一个future类型的对象，通过这个future对象可以判断任务是否执行成功，
        并且可以通过future的get方法来获取返回值，get方法会阻塞当前线程直到任务完成，而使用get（long timeout，TimeUnit unit）
        方法则会阻塞当前线程一段时间后立即返回，这时候有可能任务没有执行完。
         */
        val future: Future<*> = cachePool.submit(TestRunnable())
        val future1 = cachePool.submit(Callable<String> {
            return@Callable ""
        })
        try {
            val any = future.get()
            // 当设置1秒后返回结果，但是线程池的任务还没有执行完，会报超时异常。
            val get = future.get(1, TimeUnit.SECONDS)
            val get1 = future1.get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        /*
        原理：遍历线程池中的工作线程，然后逐个调用线程的interrupt方法来中断线程，所以无法响应中断的任务可能永远无法终止。

        区别：showdownNow首先将线程池的状态设置成STOP，然后尝试停止所有的正在执行或暂停任务的线程，并返回等待执行任务的列表，
        而showdown只是将线程池的状态设置成SHUTDOWN状态，然后中断所有没有正在执行任务的线程。

        只要调用了这两个关闭方法中的任意一个，isShutdown方法就会返回true。当所有的任务都关闭后，才表示线程池关闭成功，
        这时调用isTerminated方法会返回true。至于应该调用哪一个方法来关闭线程池，应该由提交到线程池的任务特性决定，通常调
        用shutdown方法来关闭线程池，如果任务不一定要执行完，则可以调用shutdownNow方法。
         */
        cachePool.shutdown()
        cachePool.shutdownNow()
    }

    fun testScheduledPool() {
        val schedulePool = Executors.newScheduledThreadPool(1)
        val singleThreadScheduledExecutor = Executors.newSingleThreadScheduledExecutor()
        // 延迟1分钟执行
        singleThreadScheduledExecutor.schedule(TestRunnable(), 1, TimeUnit.SECONDS)
        // 延迟3秒后执行任务，从开始执行任务这个时候开始计时，每7秒执行一次不管执行任务需要多长的时间。
        singleThreadScheduledExecutor.scheduleAtFixedRate(TestRunnable(), 3, 7, TimeUnit.SECONDS)
        // 延迟3秒后执行任务，从任务完成时这个时候开始计时，7秒后再执行，再等完成后计时7秒再执行也就是说这里的循环执行任务的时间点是从上一个任务完成的时候。
        singleThreadScheduledExecutor.scheduleWithFixedDelay(TestRunnable(), 3, 7, TimeUnit.SECONDS)
    }

}

class TestRunnable : Runnable {
    override fun run() {
        println("test thread : ${Thread.currentThread().name}")
    }
}
