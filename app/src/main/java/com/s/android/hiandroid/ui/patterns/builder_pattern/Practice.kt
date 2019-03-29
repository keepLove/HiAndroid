package com.s.android.hiandroid.ui.patterns.builder_pattern

//Product（产品角色）
internal class ProgramMonkey {
    private var mIsLiterated: Boolean = false
    private var mKnowMath: Boolean = false
    private var mLanguage: String? = null
    private var mKnowDesign: Boolean = false

    fun ismIsLiterated(): Boolean {
        return mIsLiterated
    }

    fun setmIsLiterated(mIsLiterated: Boolean) {
        this.mIsLiterated = mIsLiterated
    }

    fun ismKnowMath(): Boolean {
        return mKnowMath
    }

    fun setmKnowMath(mKnowMath: Boolean) {
        this.mKnowMath = mKnowMath
    }

    fun getmLanguage(): String? {
        return mLanguage
    }

    fun setmLanguage(mLanguage: String) {
        this.mLanguage = mLanguage
    }

    fun ismKnowDesign(): Boolean {
        return mKnowDesign
    }

    fun setmKnowDesign(mKnowDesign: Boolean) {
        this.mKnowDesign = mKnowDesign
    }

    fun show() {
        println("\rIsLiterated=" + mIsLiterated + "\n"
                + "KnowMath=" + mKnowMath + "\n"
                + "Language=" + mLanguage + "\n"
                + "KnowDesign=" + mKnowDesign + "\n")
    }
}

//Builder（抽象建造者）
internal abstract class Builder {

    abstract val monkey: ProgramMonkey
    abstract fun buildIsLiterated(arg: Boolean)
    abstract fun buildKnowMath(arg: Boolean)
    abstract fun buildLanguage(arg: String)
    abstract fun buildKnowDesign(arg: Boolean)
}

//ConcreteBuilder（具体建造者）
internal class ConcreteProgramMonkey : Builder() {
    override val monkey = ProgramMonkey()

    override fun buildIsLiterated(arg: Boolean) {
        monkey.setmIsLiterated(arg)
    }

    override fun buildKnowMath(arg: Boolean) {
        monkey.setmKnowMath(arg)
    }

    override fun buildLanguage(arg: String) {
        monkey.setmLanguage(arg)
    }

    override fun buildKnowDesign(arg: Boolean) {
        monkey.setmKnowDesign(arg)
    }
}

//Director（指挥者）
internal class Director {
    private val builder = ConcreteProgramMonkey()

    val monkeyLow: ProgramMonkey
        get() {
            builder.buildIsLiterated(true)
            builder.buildKnowMath(true)
            builder.buildLanguage("Android")
            builder.buildKnowDesign(false)

            return builder.monkey
        }

    val monkeyHigh: ProgramMonkey
        get() {
            builder.buildIsLiterated(true)
            builder.buildKnowMath(true)
            builder.buildLanguage("Android/Java/Designer")
            builder.buildKnowDesign(true)

            return builder.monkey
        }
}

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val director = Director()
        var monkey = director.monkeyLow
        monkey.show()
        monkey = director.monkeyHigh
        monkey.show()
    }
}
