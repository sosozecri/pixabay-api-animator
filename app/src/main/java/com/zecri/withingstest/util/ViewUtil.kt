package com.zecri.withingstest.util

import android.view.View

fun View.disable() {
    isEnabled = false
    alpha = 0.3f
}

fun View.enable() {
    isEnabled = true
    alpha = 1f
}

fun View.unselect() {
    alpha = 1f
}

fun View.select() {
    alpha = 0.5f
}

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}