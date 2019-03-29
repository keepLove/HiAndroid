package com.s.android.hiandroid.ui.patterns.state_pattern

class Context {
    var state: State? = null

    init {
        state = null
    }
}

interface State {
    fun doAction(context: Context)
}

class StartState : State {

    override fun doAction(context: Context) {
        println("Player is in start state")
        context.state = this
    }

    override fun toString(): String {
        return "Start State"
    }
}

class StopState : State {

    override fun doAction(context: Context) {
        println("Player is in stop state")
        context.state = this
    }

    override fun toString(): String {
        return "Stop State"
    }
}

object StatePatternDemo {
    @JvmStatic
    fun main(args: Array<String>) {
        val context = Context()

        val startState = StartState()
        startState.doAction(context)

        println(context.state!!.toString())

        val stopState = StopState()
        stopState.doAction(context)

        println(context.state!!.toString())
    }
}
