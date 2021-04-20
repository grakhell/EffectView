package ru.grakhell.effectview

import android.graphics.Bitmap
import android.renderscript.RenderScript

interface Effect {
    fun isPrepared():Boolean
    fun prepare()
    fun applyEffect(bitmap: Bitmap): Bitmap
}