package ru.grakhell.effectview
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
import android.graphics.Bitmap
import androidx.annotation.IntRange
import com.google.android.renderscript.Toolkit

class BlurEffect(
    @IntRange(from=1, to=25) radius:Int,
    listener:OnEffectSettingsChangedListener? =null
):AbstractEffect(listener) {
    private var _radius = radius

    fun setRadius(@IntRange(from=1, to=25) rad:Int){
        _radius = rad
        invalidate()
    }

    override fun applyEffect(bitmap: Bitmap): Bitmap {
        return Toolkit.blur(bitmap, _radius)
    }
}