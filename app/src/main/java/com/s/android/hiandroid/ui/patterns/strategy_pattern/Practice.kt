package com.s.android.hiandroid.ui.patterns.strategy_pattern

interface Strategy {
    fun doOperation(num1: Int, num2: Int): Int
}

class OperationAdd : Strategy {
    override fun doOperation(num1: Int, num2: Int): Int {
        return num1 + num2
    }
}

class OperationSubtract : Strategy {
    override fun doOperation(num1: Int, num2: Int): Int {
        return num1 - num2
    }
}

class OperationMultiply : Strategy {
    override fun doOperation(num1: Int, num2: Int): Int {
        return num1 * num2
    }
}

class Context(private val strategy: Strategy) {

    fun executeStrategy(num1: Int, num2: Int): Int {
        return strategy.doOperation(num1, num2)
    }
}

object StrategyPatternDemo {
    @JvmStatic
    fun main(args: Array<String>) {
        var context = Context(OperationAdd())
        println("10 + 5 = " + context.executeStrategy(10, 5))

        context = Context(OperationSubtract())
        println("10 - 5 = " + context.executeStrategy(10, 5))

        context = Context(OperationMultiply())
        println("10 * 5 = " + context.executeStrategy(10, 5))
    }
}
