package com.mackhartley.roundedprogressbarexample

import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {

    var progressColor = "#FF9B42"
    var progressTextColor = "#000000"
    var backgroundColor = "#BBBBBB"
    var backgroundTextColor = "#000000"

    var tlRadius = 12
    var trRadius = 12
    var brRadius = 12
    var blRadius = 12

    var textSize = 14
    var textPadding = 8
    var animLength = 500

    var showProgText = true
    var restrictRadius = true

}