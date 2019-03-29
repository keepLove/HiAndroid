package com.s.android.hiandroid.ui.patterns.filter_pattern

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
