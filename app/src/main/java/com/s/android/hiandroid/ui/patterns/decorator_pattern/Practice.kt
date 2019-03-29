package com.s.android.hiandroid.ui.patterns.decorator_pattern

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

class Circle : Shape {
    override fun draw() {
        println("Inside Circle::draw() method.")
    }
}

/**
 * 创建实现了 Shape 接口的抽象装饰类。
 */
abstract class ShapeDecorator(protected var decoratedShape: Shape) : Shape {

    override fun draw() {
        decoratedShape.draw()
    }
}

/**
 * 创建扩展了 ShapeDecorator 类的实体装饰类。
 */
class RedShapeDecorator(decoratedShape: Shape) : ShapeDecorator(decoratedShape) {

    override fun draw() {
        decoratedShape.draw()
        setRedBorder(decoratedShape)
    }

    private fun setRedBorder(decoratedShape: Shape) {
        println("Border Color: Red")
    }
}

object DecoratorPatternDemo {
    @JvmStatic
    fun main(args: Array<String>) {

        val circle = Circle()

        val redCircle = RedShapeDecorator(Circle())

        val redRectangle = RedShapeDecorator(Rectangle())
        println("Circle with normal border")
        circle.draw()

        println("\nCircle of red border")
        redCircle.draw()

        println("\nRectangle of red border")
        redRectangle.draw()
    }
}