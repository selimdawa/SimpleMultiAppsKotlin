package com.flatcode.simplemultiapps.utils

import android.content.Context
import android.content.Intent

fun Context.launchActivity(c: Class<*>?) {
    val intent = Intent(this, c)
    this.startActivity(intent)
}