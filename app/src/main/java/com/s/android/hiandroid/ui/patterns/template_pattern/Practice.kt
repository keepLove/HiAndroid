package com.s.android.hiandroid.ui.patterns.template_pattern

abstract class Game {

    internal abstract fun initialize()
    internal abstract fun startPlay()
    internal abstract fun endPlay()

    //模板
    fun play() {

        //初始化游戏
        initialize()

        //开始游戏
        startPlay()

        //结束游戏
        endPlay()
    }
}

class Cricket : Game() {

    internal override fun endPlay() {
        println("Cricket Game Finished!")
    }

    internal override fun initialize() {
        println("Cricket Game Initialized! Start playing.")
    }

    internal override fun startPlay() {
        println("Cricket Game Started. Enjoy the game!")
    }
}

class Football : Game() {

    internal override fun endPlay() {
        println("Football Game Finished!")
    }

    internal override fun initialize() {
        println("Football Game Initialized! Start playing.")
    }

    internal override fun startPlay() {
        println("Football Game Started. Enjoy the game!")
    }
}

object TemplatePatternDemo {
    @JvmStatic
    fun main(args: Array<String>) {

        var game: Game = Cricket()
        game.play()
        println()
        game = Football()
        game.play()
    }
}
