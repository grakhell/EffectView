package ru.grakhell.effectview.util

import kotlin.math.ceil

internal object ScalingUtil {
    private const val NORMALIZING_VALUE = 64

    @JvmStatic
    fun scale(width: Int, height:Int, scale:Float): ScaledSize {
        val scaledWidth = normalizeSize(ceil(width/scale).toInt())
        val factor = width/scaledWidth.toFloat()
        val scaledHeight = ceil(height/factor).toInt()
        return ScaledSize(scaledWidth, scaledHeight, factor)
    }

    @JvmStatic
    fun checkSize(width: Int, height:Int, scale:Float):Boolean {
        return ceil(height/scale).toInt() == 0 || ceil(width/scale).toInt() == 0
    }

    private fun normalizeSize(size:Int):Int {
        return if (size % NORMALIZING_VALUE ==0) {
            size
        } else {
            size - (size% NORMALIZING_VALUE) + NORMALIZING_VALUE
        }
    }
}