package com.example.yaroslavyadrov.cargofinder

import android.support.multidex.MultiDexApplication
import com.example.yaroslavyadrov.cargofinder.injection.component.AppComponent
import com.example.yaroslavyadrov.cargofinder.injection.component.DaggerAppComponent
import com.example.yaroslavyadrov.cargofinder.injection.module.NetworkModule
import net.danlew.android.joda.JodaTimeAndroid
import timber.log.Timber

class CargoFinderApplication : MultiDexApplication() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
        setupComponent()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun setupComponent() {
        appComponent = DaggerAppComponent.builder()
                .networkModule(NetworkModule(this))
                .build()
    }

}