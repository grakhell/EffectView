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
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.RenderEffect
import android.util.AttributeSet
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import io.github.grakhell.effectview.util.ALLOW
import io.github.grakhell.effectview.util.AutoUpdate
import io.github.grakhell.effectview.util.DISALLOW
import io.github.grakhell.effectview.util.FORCED
import io.github.grakhell.effectview.util.ScaledSize
import io.github.grakhell.effectview.util.ScalingUtil

class EffectView(
    context: Context,
    attr: AttributeSet? = null
): FrameLayout(context, attr) {

    private var _source: BitmapSource? = null
    private var _effects: MutableList<Effect> = mutableListOf()
    private var _bitmap:Bitmap? = null
    @AutoUpdate
    private var _autoUpdate:Int = ALLOW
    private val _redrawListener = ViewTreeObserver.OnPreDrawListener {

        applyEffects()
        return@OnPreDrawListener true
    }

    fun setSource(src: BitmapSource) {
        _source = src
    }

    private fun setMatrix(scaling:Float, needTranslate:Boolean = false):Matrix {
        val scale = 1f/scaling
        val mtrx = Matrix().apply {
            preScale(scale,scale)
            if (needTranslate) {
                val coords = getCoords(_source?.getScaling()?:1f)
                postTranslate(coords[0], coords[1])
            }
        }
        return mtrx
    }

    private fun applyEffects() {
        _source?.let { src ->
            if (!ScalingUtil.checkSize(width, height, src.getScaling())) {
                getSourceBitmap(src)
                _effects.forEach {
                    if(_bitmap != null) {
                        _bitmap = it.applyEffect(_bitmap!!)
                    }
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (isVisible && !isInEditMode) {
            if (_bitmap != null && _source != null) {
                applyEffects()
                canvas?.save()
                with(_source!!.getScaling()) {
                    if (this>1) {
                        canvas?.scale(this,this)
                    }
                }
                canvas?.drawBitmap(_bitmap!!,0f, 0f, null)
                canvas?.restore()
            }
        }
    }

    private fun getSize(): ScaledSize {
        return ScalingUtil.scale(measuredWidth, measuredHeight, _source?.getScaling()?:1f)
    }

    private fun getCoords(scale:Float):FloatArray {
        val coords = IntArray(2).apply {
            getLocationOnScreen(this)
        }
        val sourceCoords = _source?.getPosition()?: IntArray(2)
        val scaledCoords = FloatArray(2)
        scaledCoords[0] = -(coords[0] - sourceCoords[0])/scale
        scaledCoords[1] = -(coords[1] - sourceCoords[1])/scale
        return scaledCoords
    }

    private fun createBitmap(): Bitmap {
        val size = getSize()
        return Bitmap.createBitmap(size.width, size.height, Bitmap.Config.ARGB_8888)
    }

    private fun getSourceBitmap(src: BitmapSource): Bitmap {
        val btm: Bitmap = _bitmap ?: createBitmap()
        val m = setMatrix(src.getScaling(), src.isNeedsTranslate())
        val bitmap = src.getBitmap(btm, m)
        _bitmap = bitmap
        return bitmap
    }

    fun addEffect(effect: Effect): EffectView {
        _effects.add(effect)
        return this
    }

    fun removeEffect(effect: Effect): EffectView {
        _effects.remove(effect)
        invalidate()
        return this
    }

    fun clearEffects(): EffectView {
        _effects.clear()
        return this
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        _bitmap = createBitmap()
    }

    @RequiresApi(31)
    override fun setRenderEffect(renderEffect: RenderEffect?) {
        _source?.let { src ->
            if (!ScalingUtil.checkSize(width, height, src.getScaling())) {
                val btm: Bitmap = getSourceBitmap(src)
                val effect = RenderEffect.createBitmapEffect(btm)
                val result = renderEffect?.let { RenderEffect.createChainEffect(it, effect) }
                super.setRenderEffect(result)
                return
            }
        }
        super.setRenderEffect(renderEffect)
    }

    fun getEffects():List<Effect>  = _effects

    fun setAutoUpdate(autoupdate:Boolean) {
        this.viewTreeObserver.removeOnPreDrawListener(_redrawListener)
        _autoUpdate = if (autoupdate) {
            this.viewTreeObserver.addOnPreDrawListener(_redrawListener)
            ALLOW
        } else {
            FORCED
        }
    }

    fun getAutoUpdate() = _autoUpdate

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (_autoUpdate != FORCED) {
            _autoUpdate = ALLOW
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (_autoUpdate != FORCED) {
            _autoUpdate = DISALLOW
        }
    }


}