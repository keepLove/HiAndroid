package com.s.android.hiandroid.ui.patterns.command_pattern


//Command（抽象命令类）
internal interface Command {
    fun exec()
}

//ConcreteCommand（具体命令类）
internal class ConcreteCommand : Command {
    override fun exec() {
        val receiver = Receiver()
        receiver.action()
    }
}

//Invoker（调用者）
internal class Invoker(private var command: Command?) {

    fun setCommand(command: Command) {
        this.command = command
    }

    fun calling() {
        if (command != null) {
            command!!.exec()
        }
    }
}

//Receiver（接收者）
internal class Receiver {
    fun action() {
        println("I'm bei Called!")
    }
}

//客户端
object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val invoker = Invoker(ConcreteCommand())
        invoker.calling()
    }
}
