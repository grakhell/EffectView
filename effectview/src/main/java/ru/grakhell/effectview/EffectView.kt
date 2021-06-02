package ru.grakhell.effectview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.applyCanvas
import androidx.core.view.isVisible
import ru.grakhell.effectview.util.ScaledSize
import ru.grakhell.effectview.util.ScalingUtil
import kotlin.math.floor

class EffectView(
    context: Context,
    attr: AttributeSet? = null
): View(context, attr) {

    private var source: BitmapSource? = null
    private var effects: MutableList<Effect> = mutableListOf()
    private var pointX = -1
    private var pointY = -1
    private var viewWidth = -1
    private var viewHeight = -1
    private var bitmap:Bitmap? = null

    init {
        if (effects.size >0) {
            effects.forEach { it.prepare()}
        }
    }

    fun setSource(src:BitmapSource) {
        source= src
    }

    fun setViewPointX(x:Int) {
        pointX = x
    }

    fun setViewPointY(y:Int){
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
        if (isVisible && !isInEditMode) {
            source?.let { src ->
                if (!ScalingUtil.checkSize(width, height, src.getScaling())) {
                    bitmap = src.getBitmap(bitmap)
                    bitmap?.let {bitmap = crop(it)}
                    bitmap?.let {btmp ->
                        effects.forEach {
                            if (!it.isPrepared()) it.prepare()
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
    }

    private fun getSize(): ScaledSize {
        var wid = if (viewWidth>0) viewWidth else width
        var hei = if (viewHeight>0) viewHeight else height
        return ScalingUtil.scale(wid, hei, source?.getScaling()?:1f)
    }

    private fun getCoords(scale:Float):FloatArray {
        val coords =  if (pointX >0 && pointY >0) {
            IntArray(2).apply {
                this[0] = pointX
                this[1] = pointY
            }
        } else {
            IntArray(2).apply {
                getLocationOnScreen(this)
            }
        }
        val sourceCoords = source?.getPosition()?: IntArray(2)
        val scaledCoords = FloatArray(2)
        scaledCoords[0] = -(coords[0] - sourceCoords[0])/scale
        scaledCoords[1] = -(coords[1] - sourceCoords[1])/scale
        return scaledCoords
    }

    private fun crop(src:Bitmap):Bitmap {
        val size = getSize()
        val coords = getCoords(source?.getScaling()?:1f)
        val mtrx = Matrix().apply {
            setTranslate(coords[0], coords[1])
        }
        val btm = Bitmap.createBitmap(size.width, size.height,Bitmap.Config.ARGB_8888 )
        btm.applyCanvas {
            this.drawBitmap(src, mtrx, null)
        }
        return btm
    }

    fun addEffect(effect: Effect):EffectView {
        effects.add(effect)
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