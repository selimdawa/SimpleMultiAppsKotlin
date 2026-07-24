package com.flatcode.simplemultiapps

import android.app.Application
import io.selimdawa.multicolors.MultiColorManager

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        MultiColorManager.init(this)
    }
}