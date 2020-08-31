# RoundedProgressBar
[![Generic badge](https://img.shields.io/badge/Platform-Android-lightgray.svg)](https://developer.android.com/) [![API](https://img.shields.io/badge/API-21%2B-lightgrey.svg?style=flat)](https://android-arsenal.com/api?level=21) [![Maintenance](https://img.shields.io/badge/Maintained%3F-yes-green.svg)](https://github.com/MackHartley/RoundedProgressBar/graphs/commit-activity)

Using the `RoundedProgressBar` library you have a wide range of customizable options for making progress bars that have rounded edges. Below I've made a gif highlighting just a few examples of different looking progress bars created with this library:

<img src="/demoimgs/roundedProgressBarDemo.gif" width="400"/>

*This demo app is included in this repository under the app directory*

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
    implementation 'com.github.MackHartley:RoundedProgressBar:1.0'
}
```

# Features 🌟

1) Percentage complete text displayed on right hand side of progress bar
2) Progress bar and progress text animation upon progress change
3) The progress bar, background view, progress text and background text can all have different colors
4) The progress bar doesn't look weird at lower values (a common issue with progress bar solutions)
5) Progress bar can be set to any height and width

Additionally, the `RoundedProgressBar` handles all state on config changes including situations where there are multiple progress bars:

![Save State Demo](demoimgs/savesStateOnConfigChange.gif)

# Public Methods and Xml Attributes ⌨️
These are the methods which can be called on the RoundedProgressBar class:

Sets the progress value (between 0 and 100 inclusive)

`setProgressPercentage(progressPercentage: Double, shouldAnimate: Boolean = true)`

Gets the current progress value

`getProgressPercentage(): Double`

Sets the color of the 'progress' portion of the progress bar

`setProgressColor(colorRes: Int)`

Sets the background color of the progress bar

`setProgressBgColor(colorRes: Int)`

Sets the text size of the progress text

`setTextSize(sizeInPixels: Float)`

Sets the color of the progress text when its dtawn over the progress portion

`setTextColor(colorRes: Int)`

Sets the color of the progress text when its drawn over the background

`setBgTextColor(colorRes: Int)`

Hide or show the progress text

`showProgressText(shouldShowProgressText: Boolean)`

Set the animation length of the progress bar (in milliseconds)

`setAnimationLength(newAnimationLength: Long)`


Each setter is accessible via xml as well. Here I've mapped each setter to its corresponding xml attribute:
| Method | Xml Attribute |
|---|---|
| `setProgressPercentage(...)`  | `rpbProgress`  |
| `setProgressColor(...)`  | `rpbProgressColor`  |
| `setProgressBgColor(...)`  | `rpbProgressBgColor`  |
| `setTextSize(...)`  | `rpbTextSize`  |
| `setTextColor(...)`  | `rpbTextColor`  |
| `setBgTextColor(...)`  | `rpbBgTextColor`  |
| `showProgressText(...)`  | `rpbShowProgressText`  |
| `setAnimationLength(...)`  | `rpbAnimationLength`  |

# Why I Made This 💭

A while back I was working on an Android app which relied heavily on having a few good looking progress bars. I found [this](https://github.com/akexorcist/RoundCornerProgressBar) library which was the best option in my opinion. However, as my project progressed I encountered the following issues with it:

- No ability to set text color depending on where it's drawn
- Issues with saving state on config change when using multiple progress bars
- Occasional misplacement of text
- Looks strange at low values (Common issue with rounded progress bars)

Unfortunately, these issues weren't acceptable for my use case, and that library was the best option I could find online. So not one to settle I decided to make something better suited for what I needed.

This is not to say that library is bad, it is quite good and has many more features than my library. However, my focus with this library was to make something simple that did a good job at just being a standard '0 - 100% style' progress bar.

# Contributing 🤝
Feel free to open up issues on this repo to report bugs or request features. Additionally if you'd like to contribute to the library feel free to open up a pull request. 

Keep in mind that the goal of the library is to make a lightweight and simple rounded progress bar, so features that aren't in line with that goal might not be considered. However everyone is free to make a fork of this repo!

# License 📄
```
Copyright 2020 Mack Hartley

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
