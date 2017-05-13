package com.example.yaroslavyadrov.cargofinder

import android.support.multidex.MultiDexApplication
import com.example.yaroslavyadrov.cargofinder.injection.component.AppComponent
import com.example.yaroslavyadrov.cargofinder.injection.component.DaggerAppComponent
import net.danlew.android.joda.JodaTimeAndroid

class CargoFinderApplication : MultiDexApplication() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
        setupComponent()
    }

    private fun setupComponent() {
        appComponent = DaggerAppComponent.builder().build()
    }

}