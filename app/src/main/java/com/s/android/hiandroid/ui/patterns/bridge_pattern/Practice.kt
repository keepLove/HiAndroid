package com.s.android.hiandroid.ui.patterns.bridge_pattern

interface DrawAPI {
    fun drawCircle(radius: Int, x: Int, y: Int)
}

class RedCircle : DrawAPI {
    override fun drawCircle(radius: Int, x: Int, y: Int) {
        println(
            "Drawing Circle[ color: red, radius: "
                    + radius + ", x: " + x + ", " + y + "]"
        )
    }
}

class GreenCircle : DrawAPI {
    override fun drawCircle(radius: Int, x: Int, y: Int) {
        println(
            "Drawing Circle[ color: green, radius: "
                    + radius + ", x: " + x + ", " + y + "]"
        )
    }
}

abstract class Shape protected constructor(protected var drawAPI: DrawAPI) {
    abstract fun draw()
}

class Circle(private val x: Int, private val y: Int, private val radius: Int, drawAPI: DrawAPI) : Shape(drawAPI) {

    override fun draw() {
        drawAPI.drawCircle(radius, x, y)
    }
}

object BridgePatternDemo {
    @JvmStatic
    fun main(args: Array<String>) {
        val redCircle = Circle(100, 100, 10, RedCircle())
        val greenCircle = Circle(100, 100, 10, GreenCircle())

        redCircle.draw()
        greenCircle.draw()
    }
}
