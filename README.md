<p align="center">
    <img src="https://user-images.githubusercontent.com/10659285/113659138-06835400-9667-11eb-8f8b-5d8da5eba1c8.gif" />
</p>
<h1 align="center">RoundedProgressBar</h1>
<p align="center">Easy, Beautiful, Customizable</p>

<p align="center">
    <a href="https://android-arsenal.com/api?level=21"><img src="https://img.shields.io/badge/API-21%2B-blue.svg?style=flat" height="20"/></a>
    <a href="https://jitpack.io/#MackHartley/RoundedProgressBar"><img src="https://jitpack.io/v/MackHartley/RoundedProgressBar.svg" height="20"/></a>
    <a href="https://github.com/MackHartley/RoundedProgressBar/actions/workflows/buildAndTest.yml"><img src="https://github.com/MackHartley/RoundedProgressBar/actions/workflows/buildAndTest.yml/badge.svg" /></a>
    <a href="https://ktlint.github.io/"><img src="https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg" alt="ktlint"></a>
    <a href="https://github.com/MackHartley/RoundedProgressBar/graphs/commit-activity"><img src="https://img.shields.io/badge/Maintained%3F-yes-brightgreen.svg" height="20"/></a>
</p>

Using the `RoundedProgressBar` library you can easily create beautiful progress bars with individually rounded corners, animating progress text and more! Below are several examples of progress bars created with this library.
<br>
<p align="center">
    <img src="https://user-images.githubusercontent.com/10659285/113661686-2cf7be00-966c-11eb-91b1-5fb9c9b7ba78.gif" width="300"/>
</p>

If you’d like to see if this library is right for your project then try downloading the demo app which is available on the [Google Play Store](https://play.google.com/store/apps/details?id=com.mackhartley.roundedprogressbarexample). There you can fully customize a `RoundedProgressBar` to see if you’re able to achieve the desired look and feel for your project.
<br>
<p align="center">
    <a href='https://play.google.com/store/apps/details?id=com.mackhartley.roundedprogressbarexample&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png' height="100"/></a>
    <br>
    <img src="https://user-images.githubusercontent.com/10659285/113660889-a7274300-966a-11eb-8185-a992f485bd79.gif" width="300"/>
</p>

Do you use `RoundedProgressBar` in your app? Consider adding a picture or GIF of your usage to [`who_uses_rpb.md`](https://github.com/MackHartley/RoundedProgressBar/blob/master/who_uses_rpb.md). This provides examples to developers on how the library is used and gives your app a bit of **free publicity!**

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
    implementation 'com.github.MackHartley:RoundedProgressBar:2.0.0'
}
```

# Notable Features 🌟

1) **Animation** - Both the progress bar and progress text animate
2) **Full Color Customization** - You can even specify what color the text is depending on which background it draws over
<p align="center">
    <img src="https://user-images.githubusercontent.com/10659285/113535487-2cdabe00-9599-11eb-823f-a5256432b575.gif" />
    <br>
    <a href="exampleXmlLayouts/feature2.xml">Click here to see Xml</a>
</p>

3) **Low Value Support** - The progress bar looks nice even at low values (This is a common issue when dealing with rounded progress bars)
<p align="center">
    <img src="https://user-images.githubusercontent.com/10659285/113535772-f8b3cd00-9599-11eb-8b1a-ee6ec9e323c2.gif" />
    <br>
    <a href="exampleXmlLayouts/feature3.xml">Click here to see Xml</a>
</p>

4) **Any Corner Radius Allowed** - Individual corners can even have different radius values
<p align="center">
    <img src="https://user-images.githubusercontent.com/10659285/113536397-a83d6f00-959b-11eb-9328-84dcab86ff04.gif" />
    <br>
    <a href="exampleXmlLayouts/feature4.xml">Click here to see Xml</a>
</p>

5) **Modular** - The `RoundedProgressBar` library can be seamlessly included in custom layouts due to the fact that each corner can have a different radius
<p align="center">
    <img src="https://user-images.githubusercontent.com/10659285/113536888-cb1c5300-959c-11eb-8da1-cb6ce36d1fdd.gif" />
    <br>
    <a href="exampleXmlLayouts/feature5.xml">Click here to see Xml</a>
</p>
    
**Additionally**, the `RoundedProgressBar` handles all internal state during configuration changes (as seen below)
<p align="center">
    <img src="art/readme/savesStateOnConfigChange.gif" width="500"/>
</p>

# Public Methods and Xml Attributes ⌨️
These are the methods which can be called on the `RoundedProgressBar` class:

```
setProgressPercentage(progressPercentage: Double, shouldAnimate: Boolean = true)
getProgressPercentage(): Double

setProgressDrawableColor(@ColorInt newColor: Int) // Sets the color of the 'progress' part of the progress bar
setBackgroundDrawableColor(@ColorInt newColor: Int) // Sets the color of the 'background' part of the progress bar
setProgressTextColor(@ColorInt newColor: Int) // Sets text color for when it is drawn over the progress part of progress bar
setBackgroundTextColor(@ColorInt newColor: Int) // Sets text color for when it is drawn over the background part of progress bar

setCornerRadius(
    topLeftRadius: Float,
    topRightRadius: Float,
    bottomRightRadius: Float,
    bottomLeftRadius: Float
)

setTextSize(newTextSize: Float)
setTextPadding(newTextPadding: Float) // Sets the padding between the progress text and end (or start) of the progress bar
setAnimationLength(newAnimationLength: Long)

showProgressText(shouldShowProgressText: Boolean)
setRadiusRestricted(isRestricted: Boolean)
setOnlyShowTrue0(shouldOnlyShowTrue0: Boolean) // If true the progress bar will not allow non zero values to round down to "0%"
setOnlyShowTrue100(shouldOnlyShowTrue100: Boolean) // If true the progress bar will not allow non '100' values to round up to "100%"
```

The `RoundedProgressBar` can also be configured via xml attributes. Below is the full list of attributes along with the methods they map to.
| Xml Attribute | Method |
|---|---|
| `rpbProgress` | `setProgressPercentage(...)` |
| `rpbProgressColor` | `setProgressDrawableColor(...)` |
| `rpbBackgroundColor` | `setBackgroundDrawableColor(...)` |
| `rpbProgressTextColor` | `setProgressTextColor(...)` |
| `rpbBackgroundTextColor` | `setBackgroundTextColor(...)` |
| `rpbCornerRadius` | `setCornerRadius(...)` |
| `rpbCornerRadiusTopLeft` | `setCornerRadius(...)` |
| `rpbCornerRadiusTopRight` | `setCornerRadius(...)` |
| `rpbCornerRadiusBottomRight` | `setCornerRadius(...)` |
| `rpbCornerRadiusBottomLeft` | `setCornerRadius(...)` |
| `rpbTextSize` | `setTextSize(...)` |
| `rpbTextPadding` | `setTextPadding(...)` |
| `rpbAnimationLength` | `setAnimationLength(...)` |
| `rpbShowProgressText` | `showProgressText(...)` |
| `rpbIsRadiusRestricted` | `setRadiusRestricted(...)` |
| `rpbOnlyShowTrue0` | `setOnlyShowTrue0(...)` |
| `rpbOnlyShowTrue100` | `setOnlyShowTrue100(...)` |

# Contributing 🤝
Feel free to open up issues on this repo to report bugs or request features. 

Additionally if you'd like to contribute to the library please feel free to open up a pull request! Just give me a heads up first though (via issues or comments) so we don't overwrite each other in the event I am updating the project.

**Special thanks to all those who have supported this repo thus far!**
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
