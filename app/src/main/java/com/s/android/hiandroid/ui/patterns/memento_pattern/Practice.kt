package com.s.android.hiandroid.ui.patterns.memento_pattern

class Memento(val state: String)

class Originator {
    var state: String? = null

    fun saveStateToMemento(): Memento {
        return Memento(state!!)
    }

    fun getStateFromMemento(Memento: Memento) {
        state = Memento.state
    }
}

class CareTaker {
    private val mementoList = ArrayList<Memento>()

    fun add(state: Memento) {
        mementoList.add(state)
    }

    operator fun get(index: Int): Memento {
        return mementoList[index]
    }
}

object MementoPatternDemo {
    @JvmStatic
    fun main(args: Array<String>) {
        val originator = Originator()
        val careTaker = CareTaker()
        originator.state = "State #1"
        originator.state = "State #2"
        careTaker.add(originator.saveStateToMemento())
        originator.state = "State #3"
        careTaker.add(originator.saveStateToMemento())
        originator.state = "State #4"

        println("Current State: " + originator.state!!)
        originator.getStateFromMemento(careTaker[0])
        println("First saved State: " + originator.state!!)
        originator.getStateFromMemento(careTaker[1])
        println("Second saved State: " + originator.state!!)
    }
}
