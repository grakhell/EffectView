package ru.grakhell.effectview
/*
Copyright 2021 Dmitrii Z.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import androidx.core.graphics.applyCanvas

/**
 * Effect that's applies tint to the EffectView
 * @param color - color of tint
 * @param alpha - transparency of the tint
 * @param listener - effect settings changes listener
 */

class TintEffect(
    @ColorInt private var color:Int = Color.TRANSPARENT,
    @IntRange(from=0, to=255) private var alpha:Int = 255,
    listener:OnEffectSettingsChangedListener? =null
):AbstractEffect(listener) {

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