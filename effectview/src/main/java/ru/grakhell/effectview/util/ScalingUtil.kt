package ru.grakhell.effectview.util
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