package ru.grakhell.effectview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.renderscript.RenderScript
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import kotlin.math.abs
import kotlin.math.floor

class EffectView(
    context: Context,
    attr: AttributeSet? = null
): View(context, attr) {

    private var source: BitmapSource? = null
    private var script: RenderScript? = null
    private var effects: MutableList<Effect> = mutableListOf()
    private var pointX = x
    private var pointY = y
    private var viewWidth = width
    private var viewHeight = height
    private lateinit var bitmap:Bitmap

    init {
        script = RenderScript.create(context)
        if (effects.size >0) {
            effects.forEach { it.prepare(script)}
        }
    }

    fun setSource(src:BitmapSource) {
        source= src
    }

    fun setViewPointX(x:Float) {
        pointX = x
    }

    fun setViewPointY(y:Float){
        pointY = y
    }

    fun setViewingWidth(width:Int) {
        viewWidth = width
    }

    fun setViewingHeight(height:Int) {
        viewHeight = height
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (isVisible && width>0 && height>0) {
            source?.let { src ->
                bitmap = src.getBitmap(if (this::bitmap.isInitialized){bitmap}else {null})
                bitmap = crop(bitmap)
                effects.forEach {
                    bitmap = it.applyEffect(bitmap)
                }
                canvas?.let {cnvs ->
                    cnvs.save()
                    with(src.getScaling()) {
                        if (this>1) {
                            cnvs.scale(this.toFloat(),this.toFloat())
                        }
                    }
                    cnvs.drawBitmap(bitmap,0f, 0f, null)
                    cnvs.restore()
                }
            }
        }
    }

    private fun crop(src:Bitmap):Bitmap {
        val scale = 1f/ (source?.getScaling()?:1)
        var xt = if (pointX>=0){floor(pointX * scale).toInt()}else{0}
        var yt = if (pointY>=0){floor(pointY * scale).toInt()}else{0}
        var wid = if (viewWidth>0) {floor(viewWidth * scale).toInt()} else {1}
        var hei = if (viewHeight>0) {floor(viewHeight * scale).toInt()} else {1}
        if ((wid+xt)>src.width) {
            wid = src.width - xt
        }
        if ((hei+yt)>src.height) {
            hei = src.height - yt
        }
        if (wid<1) {
            wid = 1
        }
        if (hei<1) {
            hei = 1
        }
        return Bitmap.createBitmap(
            src,
            xt,
            yt,
            wid,
            hei
        )
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (script!= null) {
            script = null
        }
    }

    fun addEffect(effect: Effect):EffectView {
        effects.add(effect)
        if (script!= null) {
            effect.prepare(script)
        }
        return this
    }

    fun removeEffect(effect: Effect):EffectView {
        effects.remove(effect)
        invalidate()
        return this
    }

    fun getEffects():List<Effect>  = effects
}