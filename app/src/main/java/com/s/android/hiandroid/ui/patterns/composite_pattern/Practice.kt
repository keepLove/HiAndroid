package com.s.android.hiandroid.ui.patterns.composite_pattern

class Employee(private val name: String, private val dept: String, private val salary: Int) {
    private val subordinates: MutableList<Employee>

    init {
        subordinates = ArrayList()
    }

    fun add(e: Employee) {
        subordinates.add(e)
    }

    fun remove(e: Employee) {
        subordinates.remove(e)
    }

    fun getSubordinates(): List<Employee> {
        return subordinates
    }

    override fun toString(): String {
        return ("Employee :[ Name : " + name
                + ", dept : " + dept + ", salary :"
                + salary + " ]")
    }
}

object CompositePatternDemo {
    @JvmStatic
    fun main(args: Array<String>) {
        val CEO = Employee("John", "CEO", 30000)

        val headSales = Employee("Robert", "Head Sales", 20000)

        val headMarketing = Employee("Michel", "Head Marketing", 20000)

        val clerk1 = Employee("Laura", "Marketing", 10000)
        val clerk2 = Employee("Bob", "Marketing", 10000)

        val salesExecutive1 = Employee("Richard", "Sales", 10000)
        val salesExecutive2 = Employee("Rob", "Sales", 10000)

        CEO.add(headSales)
        CEO.add(headMarketing)

        headSales.add(salesExecutive1)
        headSales.add(salesExecutive2)

        headMarketing.add(clerk1)
        headMarketing.add(clerk2)

        //打印该组织的所有员工
        println(CEO)
        for (headEmployee in CEO.getSubordinates()) {
            println(headEmployee)
            for (employee in headEmployee.getSubordinates()) {
                println(employee)
            }
        }
    }
}
