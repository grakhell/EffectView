package ru.grakhell.effectview

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.renderscript.RenderScript
import androidx.annotation.ColorInt
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import kotlin.math.floor

class TintEffect(listener: OnEffectSettingsChangedListener): AbstractEffect(listener) {

    private var alpha = 0.5f
    private var colour = Color.WHITE

    fun setAlpha(alpha :Float) {
        this.alpha = alpha
    }

    fun setColor(@ColorInt color: Int) {
        colour = color
    }

    override fun applyEffect(bitmap: Bitmap): Bitmap {
        val canvas = Canvas(bitmap)
        canvas.drawARGB(
            floor(255*alpha).toInt(),
            colour.red,
            colour.green,
            colour.blue
        )
        return bitmap
    }
}