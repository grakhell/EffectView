package ru.grakhell.effectview

abstract class AbstractEffect(private val listener: OnEffectSettingsChangedListener?): Effect {

    fun invalidate() {
        listener?.onChange()
    }

    interface OnEffectSettingsChangedListener {
        fun onChange()
    }
}