package ru.grakhell.effectview

import android.graphics.Bitmap
import android.renderscript.RenderScript
import android.view.View

interface Effect {
    fun prepare(script: RenderScript?=null)
    fun applyEffect(bitmap: Bitmap): Bitmap
}