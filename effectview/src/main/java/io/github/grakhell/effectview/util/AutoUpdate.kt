package io.github.grakhell.effectview.util

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@IntDef(ALLOW, DISALLOW, FORCED)
internal annotation class AutoUpdate

internal const val ALLOW = 1
internal const val DISALLOW = 2
internal const val FORCED = 3
