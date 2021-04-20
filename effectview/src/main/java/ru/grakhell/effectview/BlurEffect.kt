package ru.grakhell.effectview

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur

class BlurEffect(context: Context, listener:OnEffectSettingsChangedListener? =null):AbstractEffect(listener) {
    private var radius = 1f
    private var blurScript: ScriptIntrinsicBlur? = null
    private var renderScript: RenderScript? = null

    init {
        renderScript = RenderScript.create(context)
        prepare()
    }

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

    override fun prepare() {
        renderScript?.let {
            blurScript = ScriptIntrinsicBlur.create(it, Element.U8_4(it))
        }
    }
}