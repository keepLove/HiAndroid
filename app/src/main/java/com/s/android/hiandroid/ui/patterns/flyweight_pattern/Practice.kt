package com.s.android.hiandroid.ui.patterns.flyweight_pattern

interface Shape {
    fun draw()
}

class Circle(private val color: String) : Shape {
    private var x: Int = 0
    private var y: Int = 0
    private var radius: Int = 0

    fun setX(x: Int) {
        this.x = x
    }

    fun setY(y: Int) {
        this.y = y
    }

    fun setRadius(radius: Int) {
        this.radius = radius
    }

    override fun draw() {
        println(
            "Circle: Draw() [Color : " + color
                    + ", x : " + x + ", y :" + y + ", radius :" + radius
        )
    }
}

/**
 * 创建一个工厂，生成基于给定信息的实体类的对象。
 */
object ShapeFactory {
    private val circleMap = HashMap<String, Circle>()

    fun getCircle(color: String): Shape {
        var circle: Circle? = circleMap.get(color)

        if (circle == null) {
            circle = Circle(color)
            circleMap.put(color, circle)
            println("Creating circle of color : $color")
        }
        return circle
    }
}

object FlyweightPatternDemo {
    private val colors = arrayOf("Red", "Green", "Blue", "White", "Black")
    private val randomColor: String
        get() = colors[(Math.random() * colors.size).toInt()]
    private val randomX: Int
        get() = (Math.random() * 100).toInt()
    private val randomY: Int
        get() = (Math.random() * 100).toInt()

    @JvmStatic
    fun main(args: Array<String>) {

        for (i in 0..19) {
            val circle = ShapeFactory.getCircle(randomColor) as Circle
            circle.setX(randomX)
            circle.setY(randomY)
            circle.setRadius(100)
            circle.draw()
        }
    }
}
