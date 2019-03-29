package com.s.android.hiandroid.ui.patterns.filter_pattern

object FilterPattern {

    val content = """
        过滤器模式（Filter Pattern）或标准模式（Criteria Pattern）是一种设计模式，这种模式允许开发人员使用不同的标准来过滤一组对象，通过逻辑运算以解耦的方式把它们连接起来。这种类型的设计模式属于结构型模式，它结合多个标准来获得单一标准。

    """.trimIndent()

    val practice = """

class Person(val name: String, val gender: String, val maritalStatus: String)

interface Criteria {
    fun meetCriteria(persons: MutableList<Person>): MutableList<Person>
}

class CriteriaMale : Criteria {

    override fun meetCriteria(persons: MutableList<Person>): MutableList<Person> {
        val malePersons = ArrayList<Person>()
        for (person in persons) {
            if (person.gender.equals("MALE", ignoreCase = true)) {
                malePersons.add(person)
            }
        }
        return malePersons
    }
}

class CriteriaFemale : Criteria {

    override fun meetCriteria(persons: MutableList<Person>): MutableList<Person> {
        val femalePersons = ArrayList<Person>()
        for (person in persons) {
            if (person.gender.equals("FEMALE", ignoreCase = true)) {
                femalePersons.add(person)
            }
        }
        return femalePersons
    }
}

class CriteriaSingle : Criteria {

    override fun meetCriteria(persons: MutableList<Person>): MutableList<Person> {
        val singlePersons = ArrayList<Person>()
        for (person in persons) {
            if (person.maritalStatus.equals("SINGLE", ignoreCase = true)) {
                singlePersons.add(person)
            }
        }
        return singlePersons
    }
}

class AndCriteria(private val criteria: Criteria, private val otherCriteria: Criteria) : Criteria {

    override fun meetCriteria(persons: MutableList<Person>): MutableList<Person> {
        val firstCriteriaPersons = criteria.meetCriteria(persons)
        return otherCriteria.meetCriteria(firstCriteriaPersons)
    }
}

class OrCriteria(private val criteria: Criteria, private val otherCriteria: Criteria) : Criteria {

    override fun meetCriteria(persons: MutableList<Person>): MutableList<Person> {
        val firstCriteriaItems = criteria.meetCriteria(persons)
        val otherCriteriaItems = otherCriteria.meetCriteria(persons)

        for (person in otherCriteriaItems) {
            if (!firstCriteriaItems.contains(person)) {
                firstCriteriaItems.add(person)
            }
        }
        return firstCriteriaItems
    }
}

object CriteriaPatternDemo {
    @JvmStatic
    fun main(args: Array<String>) {
        val persons = ArrayList<Person>()

        persons.add(Person("Robert", "Male", "Single"))
        persons.add(Person("John", "Male", "Married"))
        persons.add(Person("Laura", "Female", "Married"))
        persons.add(Person("Diana", "Female", "Single"))
        persons.add(Person("Mike", "Male", "Single"))
        persons.add(Person("Bobby", "Male", "Single"))

        val male = CriteriaMale()
        val female = CriteriaFemale()
        val single = CriteriaSingle()
        val singleMale = AndCriteria(single, male)
        val singleOrFemale = OrCriteria(single, female)

        println("Males: ")
        printPersons(male.meetCriteria(persons))

        println("\nFemales: ")
        printPersons(female.meetCriteria(persons))

        println("\nSingle Males: ")
        printPersons(singleMale.meetCriteria(persons))

        println("\nSingle Or Females: ")
        printPersons(singleOrFemale.meetCriteria(persons))
    }

    fun printPersons(persons: List<Person>) {
        for (person in persons) {
            println(
                "Person : [ Name : " + person.name
                        + ", Gender : " + person.gender
                        + ", Marital Status : " + person.maritalStatus
                        + " ]"
            )
        }
    }
}

    """.trimIndent()
}
