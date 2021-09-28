package ru.grakhell.effectview

import android.graphics.Bitmap
import androidx.annotation.IntRange
import com.google.android.renderscript.Toolkit

class BlurEffect(
    @IntRange(from=1, to=25) radius:Int,
    listener:OnEffectSettingsChangedListener? =null
):AbstractEffect(listener) {
    private var _radius = radius

    fun setRadius(@IntRange(from=1, to=25) rad:Int){
        _radius = rad
        invalidate()
    }

    override fun applyEffect(bitmap: Bitmap): Bitmap {
        return Toolkit.blur(bitmap, _radius)
    }
}