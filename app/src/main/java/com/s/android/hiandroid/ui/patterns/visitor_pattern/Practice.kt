package com.s.android.hiandroid.ui.patterns.visitor_pattern

interface ComputerPartVisitor {
    fun visit(computer: Computer)
    fun visit(mouse: Mouse)
    fun visit(keyboard: Keyboard)
    fun visit(monitor: Monitor)
}

interface ComputerPart {
    fun accept(computerPartVisitor: ComputerPartVisitor)
}

class Keyboard : ComputerPart {

    override fun accept(computerPartVisitor: ComputerPartVisitor) {
        computerPartVisitor.visit(this)
    }
}

class Monitor : ComputerPart {

    override fun accept(computerPartVisitor: ComputerPartVisitor) {
        computerPartVisitor.visit(this)
    }
}

class Mouse : ComputerPart {

    override fun accept(computerPartVisitor: ComputerPartVisitor) {
        computerPartVisitor.visit(this)
    }
}

class Computer : ComputerPart {

    private var parts: Array<ComputerPart> = arrayOf(Mouse(), Keyboard(), Monitor())

    override fun accept(computerPartVisitor: ComputerPartVisitor) {
        for (i in parts.indices) {
            parts[i].accept(computerPartVisitor)
        }
        computerPartVisitor.visit(this)
    }
}

class ComputerPartDisplayVisitor : ComputerPartVisitor {

    override fun visit(computer: Computer) {
        println("Displaying Computer.")
    }

    override fun visit(mouse: Mouse) {
        println("Displaying Mouse.")
    }

    override fun visit(keyboard: Keyboard) {
        println("Displaying Keyboard.")
    }

    override fun visit(monitor: Monitor) {
        println("Displaying Monitor.")
    }
}

object VisitorPatternDemo {
    @JvmStatic
    fun main(args: Array<String>) {

        val computer = Computer()
        computer.accept(ComputerPartDisplayVisitor())
    }
}
