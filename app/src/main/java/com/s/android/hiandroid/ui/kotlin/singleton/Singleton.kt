package com.s.android.hiandroid.ui.kotlin.singleton

/*
        kotlin实现单例的方式
 */
/**
 * 饿汉式
 */
/*
//Java实现
public class SingletonDemo {
    private static SingletonDemo instance=new SingletonDemo();
    private SingletonDemo(){

    }
    public static SingletonDemo getInstance(){
        return instance;
    }
}*/
object Singleton1 {
    fun test() {
        println("singleton1")
    }
}

/**
 * 懒汉式
 */
/*
//Java实现
public class SingletonDemo {
    private static SingletonDemo instance;
    private SingletonDemo(){}
    public static SingletonDemo getInstance(){
        if(instance==null){
            instance=new SingletonDemo();
        }
        return instance;
    }
}
*/

class Singleton2 private constructor() {
    companion object {
        private var instance: Singleton2? = null
            get() {
                if (field == null) {
                    field = Singleton2()
                }
                return field
            }

        fun get(): Singleton2 {
            return instance!!
        }
    }

    fun test() {
        println("singleton2")
    }
}

/**
 * 线程安全的懒汉式
 */
/*
//Java实现
public class SingletonDemo {
    private static SingletonDemo instance;
    private SingletonDemo(){}
    public static synchronized SingletonDemo getInstance(){//使用同步锁
        if(instance==null){
            instance=new SingletonDemo();
        }
        return instance;
    }
}
*/
class Singleton3 private constructor() {
    companion object {
        private var instance: Singleton3? = null
            get() {
                if (field == null) {
                    field = Singleton3()
                }
                return field
            }

        @Synchronized
        fun get(): Singleton3 {
            return instance!!
        }
    }

    fun test() {
        println("singleton3")
    }
}

/**
 * 双重校验锁式（Double Check)
 */
/*
//Java实现
public class SingletonDemo {
    private volatile static SingletonDemo instance;
    private SingletonDemo(){}
    public static SingletonDemo getInstance(){
        if(instance==null){
            synchronized (SingletonDemo.class){
                if(instance==null){
                    instance=new SingletonDemo();
                }
            }
        }
        return instance;
    }
}
*/
class Singleton4 private constructor() {

    companion object {
        // mode 默认就是 LazyThreadSafetyMode.SYNCHRONIZED
//        val instance: Singleton4 by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { Singleton4() }
        @Volatile
        private var instance: Singleton4? = null

        fun getInstance() =
                instance ?: synchronized(this) {
                    instance ?: Singleton4().also { instance = it }
                }
    }

    fun test() {
        println("singleton4")
    }
}

/**
 * 静态内部类式
 */
/*
//Java实现
public class SingletonDemo {
    private static class SingletonHolder{
        private static SingletonDemo instance=new SingletonDemo();
    }
    private SingletonDemo(){
        System.out.println("Singleton has loaded");
    }
    public static SingletonDemo getInstance(){
        return SingletonHolder.instance;
    }
}*/
class Singleton5 private constructor() {
    private object SingletonHolder {
        val instance = Singleton5()
    }

    fun test() {
        println("singleton5")
    }

    companion object {
        val instance: Singleton5
            get() = SingletonHolder.instance
    }
}

object Demo {
    @JvmStatic
    fun main(args: Array<String>) {
        Singleton1.test()
        Singleton2.get().test()
        Singleton3.get().test()
        Singleton4.getInstance().test()
        Singleton5.instance.test()
    }
}

val singleton_practice = """

/*
        kotlin实现单例的方式
 */
/**
 * 饿汉式
 */
/*
//Java实现
public class SingletonDemo {
    private static SingletonDemo instance=new SingletonDemo();
    private SingletonDemo(){

    }
    public static SingletonDemo getInstance(){
        return instance;
    }
}*/
object Singleton1 {
    fun test() {
        println("singleton1")
    }
}

/**
 * 懒汉式
 */
/*
//Java实现
public class SingletonDemo {
    private static SingletonDemo instance;
    private SingletonDemo(){}
    public static SingletonDemo getInstance(){
        if(instance==null){
            instance=new SingletonDemo();
        }
        return instance;
    }
}
*/

class Singleton2 private constructor() {
    companion object {
        private var instance: Singleton2? = null
            get() {
                if (field == null) {
                    field = Singleton2()
                }
                return field
            }

        fun get(): Singleton2 {
            return instance!!
        }
    }

    fun test() {
        println("singleton2")
    }
}

/**
 * 线程安全的懒汉式
 */
/*
//Java实现
public class SingletonDemo {
    private static SingletonDemo instance;
    private SingletonDemo(){}
    public static synchronized SingletonDemo getInstance(){//使用同步锁
        if(instance==null){
            instance=new SingletonDemo();
        }
        return instance;
    }
}
*/
class Singleton3 private constructor() {
    companion object {
        private var instance: Singleton3? = null
            get() {
                if (field == null) {
                    field = Singleton3()
                }
                return field
            }

        @Synchronized
        fun get(): Singleton3 {
            return instance!!
        }
    }

    fun test() {
        println("singleton3")
    }
}

/**
 * 双重校验锁式（Double Check)
 */
/*
//Java实现
public class SingletonDemo {
    private volatile static SingletonDemo instance;
    private SingletonDemo(){}
    public static SingletonDemo getInstance(){
        if(instance==null){
            synchronized (SingletonDemo.class){
                if(instance==null){
                    instance=new SingletonDemo();
                }
            }
        }
        return instance;
    }
}
*/
class Singleton4 private constructor() {

    companion object {
        // mode 默认就是 LazyThreadSafetyMode.SYNCHRONIZED
//        val instance: Singleton4 by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { Singleton4() }
        @Volatile
        private var instance: Singleton4? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: Singleton4().also { instance = it }
            }
    }

    fun test() {
        println("singleton4")
    }
}

/**
 * 静态内部类式
 */
/*
//Java实现
public class SingletonDemo {
    private static class SingletonHolder{
        private static SingletonDemo instance=new SingletonDemo();
    }
    private SingletonDemo(){
        System.out.println("Singleton has loaded");
    }
    public static SingletonDemo getInstance(){
        return SingletonHolder.instance;
    }
}*/
class Singleton5 private constructor() {
    private object SingletonHolder {
        val instance = Singleton5()
    }

    fun test() {
        println("singleton5")
    }

    companion object {
        val instance: Singleton5
            get() = SingletonHolder.instance
    }
}

object Demo {
    @JvmStatic
    fun main(args: Array<String>) {
        Singleton1.test()
        Singleton2.get().test()
        Singleton3.get().test()
        Singleton4.getInstance().test()
        Singleton5.instance.test()
    }
}

""".trimIndent()