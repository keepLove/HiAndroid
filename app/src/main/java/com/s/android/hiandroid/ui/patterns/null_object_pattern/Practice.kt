package com.s.android.hiandroid.ui.patterns.null_object_pattern

abstract class AbstractCustomer {

    abstract val name: String

    abstract val isNil: Boolean

}

class RealCustomer(override val name: String) : AbstractCustomer() {

    override val isNil: Boolean
        get() = false

}

class NullCustomer : AbstractCustomer() {

    override val name: String
        get() = "Not Available in Customer Database"

    override val isNil: Boolean
        get() = true
}

object CustomerFactory {

    val names = arrayOf("Rob", "Joe", "Julie")

    fun getCustomer(name: String): AbstractCustomer {
        for (i in names.indices) {
            if (names[i].equals(name, ignoreCase = true)) {
                return RealCustomer(name)
            }
        }
        return NullCustomer()
    }
}

object NullPatternDemo {
    @JvmStatic
    fun main(args: Array<String>) {

        val customer1 = CustomerFactory.getCustomer("Rob")
        val customer2 = CustomerFactory.getCustomer("Bob")
        val customer3 = CustomerFactory.getCustomer("Julie")
        val customer4 = CustomerFactory.getCustomer("Laura")

        println("Customers")
        println(customer1.name)
        println(customer2.name)
        println(customer3.name)
        println(customer4.name)
    }
}

