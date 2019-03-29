package com.s.android.hiandroid.ui.patterns.iterator_pattern

interface Iterator {
    operator fun hasNext(): Boolean
    operator fun next(): Any?
}

interface Container {
    val iterator: Iterator
}

class NameRepository : Container {
    var names = arrayOf("Robert", "John", "Julie", "Lora")

    override val iterator: Iterator
        get() = NameIterator()

    private inner class NameIterator : Iterator {

        internal var index: Int = 0

        override fun hasNext(): Boolean {
            return index < names.size
        }

        override fun next(): Any? {
            return if (this.hasNext()) {
                names[index++]
            } else null
        }
    }
}

object IteratorPatternDemo {

    @JvmStatic
    fun main(args: Array<String>) {
        val namesRepository = NameRepository()

        val iter = namesRepository.iterator
        while (iter.hasNext()) {
            val name = iter.next() as String
            println("Name : $name")
        }
    }
}
