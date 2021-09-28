package ru.grakhell.effectview

import android.graphics.Bitmap

interface Effect {
    fun applyEffect(bitmap: Bitmap): Bitmap
}