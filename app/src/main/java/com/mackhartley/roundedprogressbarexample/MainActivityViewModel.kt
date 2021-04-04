package com.mackhartley.roundedprogressbarexample

import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {

    private var changeAmounts = listOf(5, 10, 15, 25, 50, 100)
    private var changeAmountInd: Int = 2

    var behindProgBarColor = "#FFFFFF"
    var progressBarHeight = 40

    var progressColor = "#FF9B42"
    var progressTextColor = "#000000"
    var backgroundColor = "#BBBBBB"
    var backgroundTextColor = "#000000"

    var tlRadius = 8
    var trRadius = 8
    var brRadius = 8
    var blRadius = 8

    var textSize = 14
    var textPadding = 8
    var animLength = 500

    var showProgText = true
    var restrictRadius = true

    fun nextAmount() {
        changeAmountInd = ((changeAmountInd + 1) % (changeAmounts.size))
    }

    fun getCurAmount(): Int = changeAmounts[changeAmountInd]
}