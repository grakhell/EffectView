package io.github.grakhell.effectview
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
import android.graphics.Matrix
import androidx.annotation.FloatRange

/**
 * Base interface for bitmap sources
 */

interface BitmapSource {
    /**
     * gets bitmap from source
     * @param - previously created bitmap for reuse, or null
     * @param - matrix for image crop? null if no crop needed
     * @return - bitmap of view content transformed by matrix
     */
    fun getBitmap(dest:Bitmap, matrix:Matrix?):Bitmap

    /**
     *  sets scaling of bitmap
     */
    fun setScaling(@FloatRange(from = 1.0) scaling:Float)

    fun getScaling():Float

    /**
     *  return true if otherwise false
     */
    fun isNeedsTranslate():Boolean

    /**
     * return position of bitmap's top left corner in screen coordinates, can return null if isNeedsTranslate() returns false
     */
    fun getPosition():IntArray?
}