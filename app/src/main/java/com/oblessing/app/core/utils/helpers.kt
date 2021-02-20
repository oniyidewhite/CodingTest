package com.oblessing.app.core.utils

import android.os.Build
import android.view.View
import com.oblessing.app.BuildConfig

fun debug(invoke: () -> Unit) {
    if (BuildConfig.DEBUG) {
        invoke()
    }
}

val doNothing = {}

const val defaultCurrencySymbol = "€"

fun View.showIf(shouldShow: Boolean) {
    visibility = if (shouldShow) View.VISIBLE else View.GONE
}

fun View.hide() = showIf(false)
fun View.show() = showIf(true)
