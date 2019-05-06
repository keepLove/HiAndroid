package com.s.android.hiandroid.ui.java.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {

    private static ReentrantLock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();
    private static boolean isFlag = true;

    public static void main(String[] args) {
        OneThread oneThread = new OneThread();
        TwoThread twoThread = new TwoThread();
        oneThread.start();
        twoThread.start();
    }

    static class OneThread extends Thread {
        @Override
        public void run() {
            super.run();
            int num = 0;
            while (num++ < 5) {
                try {
                    lock.lock();
                    System.out.printf("OneThread lock\n");
                    if (!isFlag) {
                        System.out.printf("OneThread await\n");
                        condition.await();
                    }
                    System.out.printf("OneThread 被执行了\n");
                    // 查询当前线程保持这个锁的个数，也就是调用lock()的线程个数。
//                    System.out.println("getHoldCount = " + lock.getHoldCount());
                    // 查询当前处于就绪状态，正在等待获取此锁的线程个数。
//                    System.out.println("getQueueLength = " + lock.getQueueLength());
                    isFlag = false;
                    condition.signal();
                    System.out.printf("OneThread signal\n");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                    System.out.printf("OneThread unlock\n");
                }
            }
        }
    }

    static class TwoThread extends Thread {
        @Override
        public void run() {
            super.run();
            int num = 0;
            while (num++ < 5) {
                try {
                    lock.lock();
                    System.out.printf("TwoThread lock\n");
                    if (isFlag) {
                        System.out.printf("TwoThread await\n");
                        condition.await();
                    }
                    System.out.printf("TwoThread 被执行了\n");
                    isFlag = true;
                    condition.signal();
                    System.out.printf("TwoThread signal\n");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                    System.out.printf("TwoThread unlock\n");
                }
            }
        }
    }
}
