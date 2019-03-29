package com.s.android.hiandroid.ui.patterns.facade_pattern

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

class ShapeMaker {
    private val circle: Shape
    private val rectangle: Shape
    private val square: Shape

    init {
        circle = Circle()
        rectangle = Rectangle()
        square = Square()
    }

    fun drawCircle() {
        circle.draw()
    }

    fun drawRectangle() {
        rectangle.draw()
    }

    fun drawSquare() {
        square.draw()
    }
}

object FacadePatternDemo {
    @JvmStatic
    fun main(args: Array<String>) {
        val shapeMaker = ShapeMaker()

        shapeMaker.drawCircle()
        shapeMaker.drawRectangle()
        shapeMaker.drawSquare()
    }
}
