package com.s.android.hiandroid.ui.patterns.chain_pattern

abstract class AbstractLogger {

    protected var level: Int = 0

    //责任链中的下一个元素
    var nextLogger: AbstractLogger? = null

    fun logMessage(level: Int, message: String) {
        if (this.level <= level) {
            write(message)
        }
        if (nextLogger != null) {
            nextLogger!!.logMessage(level, message)
        }
    }

    protected abstract fun write(message: String)

    companion object {
        var INFO = 1
        var DEBUG = 2
        var ERROR = 3
    }
}

class ConsoleLogger(level: Int) : AbstractLogger() {

    init {
        this.level = level
    }

    override fun write(message: String) {
        println("Standard Console::Logger: $message")
    }
}

class ErrorLogger(level: Int) : AbstractLogger() {

    init {
        this.level = level
    }

    override fun write(message: String) {
        println("Error Console::Logger: $message")
    }
}

class FileLogger(level: Int) : AbstractLogger() {

    init {
        this.level = level
    }

    override fun write(message: String) {
        println("File::Logger: $message")
    }
}

object ChainPatternDemo {

    private val chainOfLoggers: AbstractLogger
        get() {

            val errorLogger = ErrorLogger(AbstractLogger.ERROR)
            val fileLogger = FileLogger(AbstractLogger.DEBUG)
            val consoleLogger = ConsoleLogger(AbstractLogger.INFO)

            errorLogger.nextLogger = fileLogger
            fileLogger.nextLogger = consoleLogger

            return errorLogger
        }

    @JvmStatic
    fun main(args: Array<String>) {
        val loggerChain = chainOfLoggers

        loggerChain.logMessage(AbstractLogger.INFO, "This is an information.")

        loggerChain.logMessage(AbstractLogger.DEBUG,
                "This is a debug level information.")

        loggerChain.logMessage(AbstractLogger.ERROR,
                "This is an error information.")
    }
}
