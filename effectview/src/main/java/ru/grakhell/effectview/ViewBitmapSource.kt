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
import android.graphics.Canvas
import android.view.View
import androidx.annotation.FloatRange
import kotlin.math.round

class ViewBitmapSource(private val view: View):BitmapSource {

    private var scaling = 1f
    private lateinit var bitmap:Bitmap

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

    override fun getBitmap(dest:Bitmap?): Bitmap {
        val scale = 1f/scaling
        val height = if (view.height>0) {round(view.height * scale).toInt()} else {1}
        val width = if (view.width>0) {round(view.width * scale).toInt()} else {1}
        bitmap = if (dest == null) {
            if(this::bitmap.isInitialized) {
                bitmap
            } else {
                Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888)
            }
        }else if(dest.width != width || dest.height != height){
            Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888)
        } else {
            dest
        }
        val canvas = Canvas(bitmap)
        if (scaling>1) {
            canvas.scale(scale,scale)
        }
        view.draw(canvas)
        return bitmap
    }
}