package com.larken.roguelike.actors

import com.halfdeadgames.kterminal.KTerminalGlyph
import com.larken.roguelike.map.Vector

open class Entity(x: Int, y: Int, val name: String, val glyph: KTerminalGlyph) {
    val pos: Vector = Vector(x,y)
    val x: Int get() = pos.x
    val y: Int get() = pos.y

    fun moveTo(x: Int, y: Int) {
        pos.set(x, y)
    }

}