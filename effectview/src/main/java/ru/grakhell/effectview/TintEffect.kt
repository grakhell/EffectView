package ru.grakhell.effectview

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import androidx.annotation.ColorInt
import androidx.core.graphics.applyCanvas

class TintEffect(
    @ColorInt private var color:Int = Color.TRANSPARENT,
    private var alpha:Int = 255,
    listener:OnEffectSettingsChangedListener? =null
):AbstractEffect(listener) {



    override fun prepare() { }

    override fun applyEffect(bitmap: Bitmap): Bitmap {
        val p = Paint().apply {
            color = this@TintEffect.color
            alpha = this@TintEffect.alpha
        }
        return bitmap.applyCanvas {
            this.drawPaint(p)
        }
    }
}