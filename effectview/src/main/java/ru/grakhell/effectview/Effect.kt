package ru.grakhell.effectview

import android.graphics.Bitmap

interface Effect {
    fun isPrepared():Boolean
    fun prepare()
    fun applyEffect(bitmap: Bitmap): Bitmap
}