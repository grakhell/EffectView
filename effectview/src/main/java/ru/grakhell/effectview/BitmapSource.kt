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
import androidx.annotation.FloatRange

/**
 * Base interface for bitmap sources
 */

interface BitmapSource {
    /**
     * gets bitmap from source
     * @param - previously created bitmap for reuse, or null
     * @return - bitmap or null if bitmap doesn't exist or can't created
     */
    fun getBitmap(dest:Bitmap?):Bitmap?

    /**
     *  sets scaling of bitmap
     */
    fun setScaling(@FloatRange(from = 1.0) scaling:Float)

    fun getScaling():Float

    /**
     * return position of bitmap's top left corner in screen coordinates
     */
    fun getPosition():IntArray?
}