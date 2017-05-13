package com.example.yaroslavyadrov.cargofinder

import android.support.multidex.MultiDexApplication
import com.example.yaroslavyadrov.cargofinder.injection.component.ApplicationComponent
import net.danlew.android.joda.JodaTimeAndroid

class CargoFinderApplication : MultiDexApplication() {
    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
        setupComponent()
    }

    private fun setupComponent() {
        appComponent = DaggerApplicationComponent.builder().build()
    }

}