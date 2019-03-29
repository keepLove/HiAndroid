package com.s.android.hiandroid.ui.patterns.mediator_pattern

import java.util.*

object ChatRoom {
    fun showMessage(user: User, message: String) {
        println(
            Date().toString()
                    + " [" + user.name + "] : " + message
        )
    }
}

class User(var name: String?) {

    fun sendMessage(message: String) {
        ChatRoom.showMessage(this, message)
    }
}

object MediatorPatternDemo {
    @JvmStatic
    fun main(args: Array<String>) {
        val robert = User("Robert")
        val john = User("John")

        robert.sendMessage("Hi! John!")
        john.sendMessage("Hello! Robert!")
    }
}

