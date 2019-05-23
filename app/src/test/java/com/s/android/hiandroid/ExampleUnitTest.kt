package com.s.android.hiandroid

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun calculate() {
        println(Math.abs(Math.sin(63.0 * Math.PI / 180)))
        println(Math.abs(Math.cos(63.0 * Math.PI / 180)))
        println(Math.abs(Math.sin(45.0 * Math.PI / 180)))
        println(Math.abs(Math.cos(45.0 * Math.PI / 180)))
        println(Math.abs(Math.sin(15.0 * Math.PI / 180)))
        println(Math.abs(Math.cos(15.0 * Math.PI / 180)))
    }
}
