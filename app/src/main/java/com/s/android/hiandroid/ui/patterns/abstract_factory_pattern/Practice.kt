package com.s.android.hiandroid.ui.patterns.abstract_factory_pattern

/**
 * 创建一个接口:
 */
interface Shape {
    fun draw()
}

/**
 * 创建实现接口的实体类。
 */
class Rectangle : Shape {
    override fun draw() {
        println("Inside Rectangle::draw() method.")
    }
}

class Square : Shape {
    override fun draw() {
        println("Inside Square::draw() method.")
    }
}

class Circle : Shape {
    override fun draw() {
        println("Inside Circle::draw() method.")
    }
}

/**
 * 为颜色创建一个接口。
 */
interface Color {
    fun fill()
}

/**
 * 创建实现接口的实体类。
 */
class Red : Color {

    override fun fill() {
        println("Inside Red::fill() method.")
    }
}

class Green : Color {

    override fun fill() {
        println("Inside Green::fill() method.")
    }
}

class Blue : Color {

    override fun fill() {
        println("Inside Blue::fill() method.")
    }
}

/**
 * 为 Color 和 Shape 对象创建抽象类来获取工厂。
 */
abstract class AbstractFactory {
    abstract fun getColor(color: String?): Color?
    abstract fun getShape(shape: String?): Shape?
}

/**
 * 创建一个工厂，生成基于给定信息的实体类的对象。
 */
class ShapeFactory : AbstractFactory() {

    override fun getColor(color: String?): Color? {
        return null
    }

    override fun getShape(shape: String?): Shape? {
        if (shape == null) {
            return null
        }
        return when {
            shape.equals("CIRCLE", ignoreCase = true) -> Circle()
            shape.equals("RECTANGLE", ignoreCase = true) -> Rectangle()
            shape.equals("SQUARE", ignoreCase = true) -> Square()
            else -> null
        }
    }

}

class ColorFactory : AbstractFactory() {

    override fun getShape(shape: String?): Shape? {
        return null
    }

    override fun getColor(color: String?): Color? {
        if (color == null) {
            return null
        }
        return when {
            color.equals("RED", ignoreCase = true) -> Red()
            color.equals("GREEN", ignoreCase = true) -> Green()
            color.equals("BLUE", ignoreCase = true) -> Blue()
            else -> null
        }
    }
}

/**
 * 创建一个工厂创造器/生成器类，通过传递形状或颜色信息来获取工厂。
 */
object FactoryProducer {
    fun getFactory(choice: String): AbstractFactory? {
        if (choice.equals("SHAPE", ignoreCase = true)) {
            return ShapeFactory()
        } else if (choice.equals("COLOR", ignoreCase = true)) {
            return ColorFactory()
        }
        return null
    }
}

/**
 * 使用 FactoryProducer 来获取 AbstractFactory，通过传递类型信息来获取实体类的对象。
 */
object AbstractFactoryPatternDemo {
    @JvmStatic
    fun main(args: Array<String>) {

        //获取形状工厂
        val shapeFactory = FactoryProducer.getFactory("SHAPE")

        //获取形状为 Circle 的对象
        val shape1 = shapeFactory!!.getShape("CIRCLE")

        //调用 Circle 的 draw 方法
        shape1!!.draw()

        //获取形状为 Rectangle 的对象
        val shape2 = shapeFactory.getShape("RECTANGLE")

        //调用 Rectangle 的 draw 方法
        shape2!!.draw()

        //获取形状为 Square 的对象
        val shape3 = shapeFactory.getShape("SQUARE")

        //调用 Square 的 draw 方法
        shape3!!.draw()

        //获取颜色工厂
        val colorFactory = FactoryProducer.getFactory("COLOR")

        //获取颜色为 Red 的对象
        val color1 = colorFactory!!.getColor("RED")

        //调用 Red 的 fill 方法
        color1!!.fill()

        //获取颜色为 Green 的对象
        val color2 = colorFactory.getColor("Green")

        //调用 Green 的 fill 方法
        color2!!.fill()

        //获取颜色为 Blue 的对象
        val color3 = colorFactory.getColor("BLUE")

        //调用 Blue 的 fill 方法
        color3!!.fill()
    }
}