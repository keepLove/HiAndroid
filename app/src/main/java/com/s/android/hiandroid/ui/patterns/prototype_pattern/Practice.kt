package com.s.android.hiandroid.ui.patterns.prototype_pattern

import java.util.*


/**
 * 创建一个实现了 Cloneable 接口的抽象类。
 */
abstract class Shape : Cloneable {

    var id: String? = null
    var type: String? = null
        protected set

    internal abstract fun draw()

    public override fun clone(): Any {
        var clone: Any? = null
        try {
            clone = super.clone()
        } catch (e: CloneNotSupportedException) {
            e.printStackTrace()
        }

        return clone!!
    }
}

/**
 * 创建扩展了上面抽象类的实体类。
 */
class Rectangle : Shape() {
    init {
        type = "Rectangle"
    }

    override fun draw() {
        println("Inside Rectangle::draw() method.")
    }
}

class Square : Shape() {
    init {
        type = "Square"
    }

    override fun draw() {
        println("Inside Square::draw() method.")
    }
}

class Circle : Shape() {
    init {
        type = "Circle"
    }

    override fun draw() {
        println("Inside Circle::draw() method.")
    }
}

/**
 * 创建一个类，从数据库获取实体类，并把它们存储在一个 Hashtable 中。
 */
object ShapeCache {

    private val shapeMap = Hashtable<String, Shape>()

    fun getShape(shapeId: String): Shape {
        val cachedShape = shapeMap[shapeId]
        return cachedShape?.clone() as Shape
    }

    // 对每种形状都运行数据库查询，并创建该形状
    // shapeMap.put(shapeKey, shape);
    // 例如，我们要添加三种形状
    fun loadCache() {
        val circle = Circle()
        circle.id = "1"
        shapeMap.put(circle.id, circle)

        val square = Square()
        square.id = "2"
        shapeMap.put(square.id, square)

        val rectangle = Rectangle()
        rectangle.id = "3"
        shapeMap.put(rectangle.id, rectangle)
    }
}

object PrototypePatternDemo {
    @JvmStatic
    fun main(args: Array<String>) {
        ShapeCache.loadCache()

        println("Shape : " + ShapeCache.getShape("1").type!!)

        println("Shape : " + ShapeCache.getShape("2").type!!)

        println("Shape : " + ShapeCache.getShape("3").type!!)
    }
}
