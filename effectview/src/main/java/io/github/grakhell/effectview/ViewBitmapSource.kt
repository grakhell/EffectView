package io.github.grakhell.effectview
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
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.view.View
import androidx.annotation.FloatRange

/**
 *  Bitmap source from view
 */

class ViewBitmapSource(private val view: View): BitmapSource {

    private var scaling = 1f

    override fun setScaling( @FloatRange(from = 1.0) scaling: Float) {
        if (scaling<1f) return
        this.scaling = scaling
    }

    override fun getPosition(): IntArray {
        val arr = IntArray(2)
        view.getLocationOnScreen(arr)
        return arr
    }

    override fun getScaling(): Float = scaling

    override fun isNeedsTranslate(): Boolean = true

    override fun getBitmap(dest: Bitmap, matrix: Matrix?): Bitmap {
        val bitmap = dest
        val canvas = Canvas(bitmap)
        if (matrix != null) {
            canvas.save()
            canvas.setMatrix(matrix)
            view.draw(canvas)
            canvas.restore()
        } else {
            view.draw(canvas)
        }
        return bitmap
    }
}