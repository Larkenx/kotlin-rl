package com.larken.roguelike.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.larken.roguelike.Game
import com.larken.roguelike.constants.*

class DesktopLauncher {
    fun main() {
        val config = LwjglApplicationConfiguration()
        config.title = "${TITLE} v${VERSION}"
        config.width = V_WIDTH
        config.height = V_HEIGHT
        config.backgroundFPS = FPS
        config.foregroundFPS = FPS
        config.resizable = RESIZEABLE
        LwjglApplication(Game(), config)
    }
}

fun main(arg: Array<String>) {
    DesktopLauncher().main()
}
