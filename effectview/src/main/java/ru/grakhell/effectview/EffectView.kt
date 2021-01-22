package ru.grakhell.effectview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.renderscript.RenderScript
import android.util.AttributeSet
import android.view.View
import androidx.core.view.isVisible
import kotlin.math.floor

class EffectView(
    context: Context,
    attr: AttributeSet? = null
): View(context, attr) {

    private var source: BitmapSource? = null
    private var script: RenderScript? = null
    private var effects: MutableList<Effect> = mutableListOf()
    private var pointX = -1f
    private var pointY = -1f
    private var viewWidth = -1
    private var viewHeight = -1
    private var bitmap:Bitmap? = null

    init {
        if(!isInEditMode) script = RenderScript.create(context)
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
        if (isVisible && width>0 && height>0 && !isInEditMode) {
            source?.let { src ->

                bitmap = src.getBitmap(bitmap)
                bitmap?.let {bitmap = crop(it)}
                bitmap?.let {btmp ->
                    effects.forEach {
                        if (!it.isPrepared()) it.prepare(script)
                        bitmap = it.applyEffect(btmp)
                    }
                    canvas?.let {cnvs ->
                        cnvs.save()
                        with(src.getScaling()) {
                            if (this>1) {
                                cnvs.scale(this.toFloat(),this.toFloat())
                            }
                        }
                        cnvs.drawBitmap(btmp,0f, 0f, null)
                        cnvs.restore()
                    }
                }
            }
        }
    }

    private fun crop(src:Bitmap):Bitmap {
        val scale = 1f/ (source?.getScaling()?:1)
        var xt = if (pointX>=0){floor(pointX * scale).toInt()}else{floor(x * scale).toInt()}
        var yt = if (pointY>=0){floor(pointY * scale).toInt()}else{floor(y * scale).toInt()}
        var wid = if (viewWidth>0) {floor(viewWidth * scale).toInt()} else {floor(width * scale).toInt()}
        var hei = if (viewHeight>0) {floor(viewHeight * scale).toInt()} else {floor(height * scale).toInt()}
        if (xt>=src.width){
            xt =src.width - wid
            if (xt<0) {
                xt = 0
                wid = src.width
            }
        }
        if (yt>=src.height) {
            yt = src.height - hei
            if (yt<0) {
                yt = 0
                hei =src.height
            }
        }
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

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (script == null && !isInEditMode) {
            script = RenderScript.create(context)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (script!= null && !isInEditMode) {
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

    fun clearEffects():EffectView {
        effects.clear()
        return this
    }

    fun getEffects():List<Effect>  = effects
}