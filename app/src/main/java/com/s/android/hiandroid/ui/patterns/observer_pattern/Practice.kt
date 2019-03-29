package com.s.android.hiandroid.ui.patterns.observer_pattern


class Subject {

    private val observers = ArrayList<Observer>()
    var state: Int = 0
        set(state) {
            field = state
            notifyAllObservers()
        }

    fun attach(observer: Observer) {
        observers.add(observer)
    }

    fun notifyAllObservers() {
        for (observer in observers) {
            observer.update()
        }
    }
}

abstract class Observer {
    protected var subject: Subject? = null
    abstract fun update()
}

class BinaryObserver(subject: Subject) : Observer() {

    init {
        this.subject = subject
        this.subject!!.attach(this)
    }

    override fun update() {
        println("Binary String: " + Integer.toBinaryString(subject!!.state))
    }
}

class OctalObserver(subject: Subject) : Observer() {

    init {
        this.subject = subject
        this.subject!!.attach(this)
    }

    override fun update() {
        println("Octal String: " + Integer.toOctalString(subject!!.state))
    }
}

class HexaObserver(subject: Subject) : Observer() {

    init {
        this.subject = subject
        this.subject!!.attach(this)
    }

    override fun update() {
        println("Hex String: " + Integer.toHexString(subject!!.state).toUpperCase())
    }
}

object ObserverPatternDemo {
    @JvmStatic
    fun main(args: Array<String>) {
        val subject = Subject()

        HexaObserver(subject)
        OctalObserver(subject)
        BinaryObserver(subject)

        println("First state change: 15")
        subject.state = 15
        println("Second state change: 10")
        subject.state = 10
    }
}
