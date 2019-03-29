package com.s.android.hiandroid.ui.patterns.proxy_pattern

interface Image {
    fun display()
}

class RealImage(private val fileName: String) : Image {

    init {
        loadFromDisk(fileName)
    }

    override fun display() {
        println("Displaying $fileName")
    }

    private fun loadFromDisk(fileName: String) {
        println("Loading $fileName")
    }
}

class ProxyImage(private val fileName: String) : Image {

    private var realImage: RealImage? = null

    override fun display() {
        if (realImage == null) {
            realImage = RealImage(fileName)
        }
        realImage!!.display()
    }
}

object ProxyPatternDemo {

    @JvmStatic
    fun main(args: Array<String>) {
        val image = ProxyImage("test_10mb.jpg")

        // 图像将从磁盘加载
        image.display()
        println("")
        // 图像不需要从磁盘加载
        image.display()
    }
}
