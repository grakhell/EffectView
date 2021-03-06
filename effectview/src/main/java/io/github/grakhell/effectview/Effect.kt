package io.github.grakhell.effectview

import android.graphics.Bitmap
import android.graphics.Matrix
import java.util.WeakHashMap

/*
Copyright 2022 Dmitrii Z.

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

/**
 * Base class for effects
 */

abstract class Effect(private val src:BitmapSource): BitmapSource {

    private val _listeners:WeakHashMap<OnEffectSettingsChangedListener, String> = WeakHashMap()

    abstract fun applyEffect(bitmap: Bitmap):Bitmap

    override fun getBitmap(dest: Bitmap, matrix: Matrix?): Bitmap {
        val b = src.getBitmap(dest, matrix)
        return applyEffect(b)
    }

    override fun isNeedsTranslate(): Boolean = src.isNeedsTranslate()

    override fun getPosition(): IntArray? = src.getPosition()

    override fun getScaling(): Float =  src.getScaling()

    override fun setScaling(scaling: Float) {
        src.setScaling(scaling)
    }

    fun invalidate() {
        _listeners.forEach {
            it.key?.onChange()
        }
    }

    fun addListener(listener: OnEffectSettingsChangedListener?) {
        _listeners[listener] = listener.toString()
    }
}