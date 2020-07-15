package ru.grakhell.effectview

import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.view.View

class BlurEffect(listener:OnEffectSettingsChangedListener? =null):AbstractEffect(listener) {
    private var radius = 1f
    private var blurScript: ScriptIntrinsicBlur? = null
    private var renderScript: RenderScript? = null


    fun setRadius(rad:Int){
        radius = rad.toFloat()
        invalidate()
    }

    override fun applyEffect(bitmap: Bitmap): Bitmap {
        val input = Allocation.createFromBitmap(renderScript, bitmap)
        val output = Allocation.createTyped(renderScript, input.type)
        blurScript?.setRadius(radius)
        blurScript?.setInput(input)
        blurScript?.forEach(output)
        output.copyTo(bitmap)
        return bitmap
    }

    override fun prepare(script: RenderScript?) {
        renderScript = script
        blurScript = ScriptIntrinsicBlur.create(script, Element.U8_4(script))
    }
}