<p align="center">
    <img src="demoimgs/rpbShowcase.gif" alt="animated" />
</p>
<h1 align="center">RoundedProgressBar</h1>
<p align="center">Easy, Beautiful, Customizeable</p>

<p align="center">
    <a href="https://developer.android.com/"><img src="https://img.shields.io/badge/Platform-Android-green.svg" height="20"/></a>
    <a href="https://android-arsenal.com/api?level=21"><img src="https://img.shields.io/badge/API-21%2B-blue.svg?style=flat" height="20"/></a>
    <a href="https://github.com/MackHartley/RoundedProgressBar/actions/workflows/buildAndTest.yml"><img src="https://github.com/MackHartley/RoundedProgressBar/actions/workflows/buildAndTest.yml/badge.svg" /></a>
    <a href="https://github.com/MackHartley/RoundedProgressBar/issues"><img src="https://img.shields.io/github/issues/mackhartley/roundedprogressbar.svg" height="20"/></a>
    <a href="https://github.com/MackHartley/RoundedProgressBar/graphs/commit-activity"><img src="https://img.shields.io/badge/Maintained%3F-yes-brightgreen.svg" height="20"/></a>
</p>

The `RoundedProgressBar` library gives you a wide range of customizable options for making progress bars that have rounded edges. Below I've made a gif highlighting a few examples of different looking progress bars created with this library:

<p align="center">
    <img src="/demoimgs/demoApp.gif" width="400"/>
    <br>
    <i>This demo app is included in this repository under the app directory</i>
</p>

# Gradle Setup ⚙️
[![](https://jitpack.io/v/MackHartley/RoundedProgressBar.svg)](https://jitpack.io/#MackHartley/RoundedProgressBar)

If you don't have this already, add it to your **root** build.gradle file:
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Then you can add the dependency to your **app** build.gradle file:
```
dependencies {
    ...
    implementation 'com.github.MackHartley:RoundedProgressBar:1.0.1'
}
```

# Features 🌟

1) **Completion Text** - Displays `00%` percentage completion of the progress bar
2) **Animaition** - Both the progress bar and progress text animate
3) **Foreground / Background** - Both the foreground (aka progress) and background colors can be set
4) **Separate Text Colors** - You can specify the text color based on whether it's shown over the foreground or background
5) **Low Value Support** - The progress bar looks nice even at low values! (This is a common issue when dealing with rounded progress bars)
<p align="center">
    <img src="demoimgs/differentTextColors.gif" width="600"/>
</p>

6) **Corner Radius** - Any corner radius value can be set (You can even remove it if you just wanted the animated progress bar)
<p align="center">
    <img src="demoimgs/anyCornerRadius.gif" width="600"/>
</p>

**Additionally**, the `RoundedProgressBar` handles all state on config changes including situations where there are multiple progress bars:
<p align="center">
    <img src="demoimgs/savesStateOnConfigChange.gif" width="600"/>
</p>

# Public Methods and Xml Attributes ⌨️
These are the methods which can be called on the RoundedProgressBar class:

```
setProgressPercentage(progressPercentage: Double, shouldAnimate: Boolean = true)
getProgressPercentage(): Double

setProgressDrawableColor(@ColorInt newColor: Int) // Sets the color of the 'progress' part of the progress bar
setBackgroundDrawableColor(@ColorInt newColor: Int) // Sets the color of the progress bar background
setProgressTextColor(@ColorInt newColor: Int) // Sets text color for when it is drawn over the progress part of progress bar
setBackgroundTextColor(@ColorInt newColor: Int) // Sets text color for when it is drawn over the progress bar background

setCornerRadius(
    topLeftRadius: Float,
    topRightRadius: Float,
    bottomRightRadius: Float,
    bottomLeftRadius: Float
)

setTextSize(newTextSize: Float)
setTextPadding(newTextPadding: Float)
setAnimationLength(newAnimationLength: Long)

showProgressText(shouldShowProgressText: Boolean) // Hide or show the progress text
setRadiusRestricted(isRestricted: Boolean)
```

Each setter is accessible via xml as well. Here I've mapped each setter to its corresponding xml attribute:
| Method | Xml Attribute |
|---|---|
| `setProgressPercentage(...)`  | `rpbProgress`  |
| `setProgressDrawableColor(...)`  | `rpbProgressColor`  |
| `setBackgroundDrawableColor(...)`  | `rpbBackgroundColor`  |
| `setProgressTextColor(...)`  | `rpbProgressTextColor`  |
| `setBackgroundTextColor(...)`  | `rpbBackgroundTextColor`  |
| `setCornerRadius(...)`  | `rpbCornerRadius`  |
| `setCornerRadius(...)`  | `rpbCornerRadiusTopLeft`  |
| `setCornerRadius(...)`  | `rpbCornerRadiusTopRight`  |
| `setCornerRadius(...)`  | `rpbCornerRadiusBottomRight`  |
| `setCornerRadius(...)`  | `rpbCornerRadiusBottomLeft`  |
| `setTextSize(...)`  | `rpbTextSize`  |
| `setTextPadding(...)`  | `rpbTextPadding`  |
| `setAnimationLength(...)`  | `rpbAnimationLength`  |
| `showProgressText(...)`  | `rpbShowProgressText`  |
| `setRadiusRestricted(...)`  | `rpbIsRadiusRestricted`  |
<p align="center">
    <br>
    <img src="/demoimgs/progressBarDiagram.png" width="600"/>
    <br>
    <i>This diagram shows which methods are responsible for setting the different colors of the progress bar</i>
</p>

# Why I Made This 💭

A while back I was working on an Android app which relied heavily on having a few good looking progress bars. I found [this](https://github.com/akexorcist/RoundCornerProgressBar) library which was the best option in my opinion. However, as my project progressed I encountered the following issues with it:

- No ability to set text color depending on where it's drawn
- Occasional misplacement of text
- Looks strange at low values (Common issue with rounded progress bars)
- Doesn't have text animation

Unfortunately, these issues weren't acceptable for my use case, and that library was the best option I could find online. So not one to settle I decided to make something better suited for what I needed.

This is not to say that library is bad, it is quite good and has many more features than my library. However, my focus with this library was to make something simple that did a good job at just being a standard '0 - 100% style' progress bar.

# Contributing 🤝
Feel free to open up issues on this repo to report bugs or request features. 

Additionally if you'd like to contribute to the library feel free to open up a pull request! Just give me a heads up first though (via issues or comments) so we don't overwrite each other.

Special thanks to all those who have supported this repo thus far!
<p align="center">
    <br>
    <a href="https://github.com/MackHartley/RoundedProgressBar/stargazers"><img src="https://reporoster.com/stars/MackHartley/RoundedProgressBar"/></a>
    <br>
    <a href="https://github.com/MackHartley/RoundedProgressBar/network/members"><img src="https://reporoster.com/forks/MackHartley/RoundedProgressBar"/></a>
</p>

# License 📄
```
Copyright 2021 Mack Hartley

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
