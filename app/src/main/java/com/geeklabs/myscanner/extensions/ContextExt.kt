package com.geeklabs.myscanner.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat

fun Context.color(@ColorRes colorResource: Int): Int {
    return ContextCompat.getColor(this, colorResource)
}

fun Context.drawable(@DrawableRes drawableResource: Int): Drawable? {
    return ContextCompat.getDrawable(this, drawableResource)
}

fun Context.inflate(@LayoutRes layoutRes: Int): View? {
    return LayoutInflater.from(this).inflate(layoutRes, null)
}