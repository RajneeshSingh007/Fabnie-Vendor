package com.fabnie.vendor

import android.app.Application
import com.fabnie.vendor.utils.AppPref
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication: Application() {

    companion object {
        lateinit var appContext: BaseApplication
    }

    lateinit var appPref: AppPref

    override fun onCreate() {
        super.onCreate()
        appContext = this
        appPref = AppPref(context = this)
    }
}