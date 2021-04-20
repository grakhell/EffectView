package ru.grakhell.effectview

import android.graphics.Bitmap

interface BitmapSource {
    fun getBitmap(dest:Bitmap?):Bitmap?
    fun setScaling(scaling:Float)
    fun getScaling():Float
    fun getPosition():IntArray?
}