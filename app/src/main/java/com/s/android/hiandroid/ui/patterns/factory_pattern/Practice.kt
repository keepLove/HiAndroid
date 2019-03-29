package com.s.android.hiandroid.ui.patterns.factory_pattern

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
 * 创建一个工厂，生成基于给定信息的实体类的对象。
 */
class ShapeFactory {

    //使用 getShape 方法获取形状类型的对象
    fun getShape(shapeType: String?): Shape? {
        if (shapeType == null) {
            return null
        }
        return when {
            shapeType.equals("CIRCLE", ignoreCase = true) -> Circle()
            shapeType.equals("RECTANGLE", ignoreCase = true) -> Rectangle()
            shapeType.equals("SQUARE", ignoreCase = true) -> Square()
            else -> null
        }
    }
}

/**
 * 使用该工厂，通过传递类型信息来获取实体类的对象。
 */
object FactoryPatternDemo {

    @JvmStatic
    fun main(args: Array<String>) {
        val shapeFactory = ShapeFactory()
        //获取 Circle 的对象，并调用它的 draw 方法
        val shape1 = shapeFactory.getShape("CIRCLE")
        //调用 Circle 的 draw 方法
        shape1!!.draw()
        //获取 Rectangle 的对象，并调用它的 draw 方法
        val shape2 = shapeFactory.getShape("RECTANGLE")
        //调用 Rectangle 的 draw 方法
        shape2!!.draw()
        //获取 Square 的对象，并调用它的 draw 方法
        val shape3 = shapeFactory.getShape("SQUARE")
        //调用 Square 的 draw 方法
        shape3!!.draw()
    }
}
