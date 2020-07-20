package ru.grakhell.effectview

import android.renderscript.RenderScript

abstract class AbstractEffect(private val listener: OnEffectSettingsChangedListener?): Effect {

    private var prepared = false

    override fun isPrepared(): Boolean =prepared

    override fun prepare(script: RenderScript?) {
        prepared = true
    }

    fun invalidate() {
        listener?.onChange()
    }

    interface OnEffectSettingsChangedListener {
        fun onChange()
    }
}