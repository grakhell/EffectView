# Effect View
A view that allows you to apply several effects to a bitmap obtained from a configurable source

It takes bitmap from a source and apply multiple effects to it then draw it on self canvas.
Effect View redraws its content when changes in view hierarchy are detected (draw() called).

## Download
[![Maven Central](https://img.shields.io/maven-central/v/io.github.grakhell/EffectView.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.grakhell%22%20AND%20a:%22EffectView%22)

Grab via Gradle:

groovy
```groovy
    implementation 'io.github.grakhell:EffectView:$latest_version'
``` 
kotlin dsl
```kotlin
    implementation("io.github.grakhell:EffectView:$latest_version")
```

for extensions with bundled effects

groovy
```groovy
    implementation 'io.github.grakhell:EffectView-ext:$latest_version'
``` 
kotlin dsl
```kotlin
    implementation("io.github.grakhell:EffectView-ext:$latest_version")
```

## How to use
```XML
<io.github.grakhell.effectview.EffectView
	android:layout_width="match_parent"
	android:layout_height="match_parent"/>
```

EffectView for work required bitmap source. By default bitmap source from view is provided.
```kotlin
//ViewGroup or View you want to start effects from. Choose root as close to EffectView in hierarchy as possible.
//Always try to choose the closest possible layout or view to EffectView.
val contentImageView = (View) getWindow().getDecorView().findViewById(android.R.id.content);
val src = ViewBitmapSource(contentImageView)
```

EffectView can apply multiple effects at once. By default blur and tint effects is provided, but you can create your own effects by overriding base Effect class.
Default blur effect uses [Renderscript Toolkit](https://github.com/android/renderscript-intrinsics-replacement-toolkit "")
```kotlin
val blurEffect = BlurEffect(src, radius = 5)
val tintEffect = TintEffect(blurEffect, color = Color.BLACK, alpha = 60)
effectView.apply {
    setSource(tintEffect) // sets source
}
```

## Licence
```
Copyright 2022 Dmitrii Z.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```