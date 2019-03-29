package com.s.android.hiandroid.ui.patterns.builder_pattern

object BuilderPattern {

    val content = """
        建造者模式（Builder Pattern）使用多个简单的对象一步一步构建成一个复杂的对象。这种类型的设计模式属于创建型模式，它提供了一种创建对象的最佳方式。

一个 Builder 类会一步一步构造最终的对象。该 Builder 类是独立于其他对象的。

意图：将一个复杂的构建与其表示相分离，使得同样的构建过程可以创建不同的表示。

主要解决：主要解决在软件系统中，有时候面临着"一个复杂对象"的创建工作，其通常由各个部分的子对象用一定的算法构成；由于需求的变化，这个复杂对象的各个部分经常面临着剧烈的变化，但是将它们组合在一起的算法却相对稳定。

何时使用：一些基本部件不会变，而其组合经常变化的时候。

如何解决：将变与不变分离开。

关键代码：建造者：创建和提供实例，导演：管理建造出来的实例的依赖关系。

应用实例： 1、去肯德基，汉堡、可乐、薯条、炸鸡翅等是不变的，而其组合是经常变化的，生成出所谓的"套餐"。 2、JAVA 中的 StringBuilder。

优点： 1、建造者独立，易扩展。 2、便于控制细节风险。

缺点： 1、产品必须有共同点，范围有限制。 2、如内部变化复杂，会有很多的建造类。

使用场景： 1、需要生成的对象具有复杂的内部结构。 2、需要生成的对象内部属性本身相互依赖。

注意事项：与工厂模式的区别是：建造者模式更加关注与零件装配的顺序。
    """.trimIndent()

    val practice = """

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
    """.trimIndent()
}
