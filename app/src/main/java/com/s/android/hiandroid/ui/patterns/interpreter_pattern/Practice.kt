package com.s.android.hiandroid.ui.patterns.interpreter_pattern

interface Expression {
    fun interpret(context: String): Boolean
}

class TerminalExpression(private val data: String) : Expression {

    override fun interpret(context: String): Boolean {
        return context.contains(data)
    }
}

class OrExpression(val expr1: Expression, val expr2: Expression) : Expression {

    override fun interpret(context: String): Boolean {
        return expr1.interpret(context) || expr2.interpret(context)
    }
}

class AndExpression(val expr1: Expression, val expr2: Expression) : Expression {

    override fun interpret(context: String): Boolean {
        return expr1.interpret(context) && expr2.interpret(context)
    }
}

object InterpreterPatternDemo {

    //规则：Robert 和 John 是男性
    val maleExpression: Expression
        get() {
            val robert = TerminalExpression("Robert")
            val john = TerminalExpression("John")
            return OrExpression(robert, john)
        }

    //规则：Julie 是一个已婚的女性
    val marriedWomanExpression: Expression
        get() {
            val julie = TerminalExpression("Julie")
            val married = TerminalExpression("Married")
            return AndExpression(julie, married)
        }

    @JvmStatic
    fun main(args: Array<String>) {
        val isMale = maleExpression
        val isMarriedWoman = marriedWomanExpression

        println("John is male? " + isMale.interpret("John"))
        println("Julie is a married women? " + isMarriedWoman.interpret("Married Julie"))
    }
}

