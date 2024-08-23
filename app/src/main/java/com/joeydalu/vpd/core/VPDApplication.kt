package com.joeydalu.vpd.core

import android.app.Application
import androidx.multidex.MultiDex
import dagger.hilt.android.HiltAndroidApp

/**
 * Android application class implementation.
 * @author Joseph Dalughut
 */
@HiltAndroidApp
class VPDApplication: Application() {


    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
    }

}