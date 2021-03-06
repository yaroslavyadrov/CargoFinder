package ru.mydispatcher

import android.support.multidex.MultiDexApplication
import ru.mydispatcher.injection.component.AppComponent
import ru.mydispatcher.injection.component.DaggerAppComponent
import ru.mydispatcher.injection.module.NetworkModule
import net.danlew.android.joda.JodaTimeAndroid
import timber.log.Timber

class CargoFinderApplication : MultiDexApplication() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
        setupComponent()
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return super.createStackElementTag(element) + ':' + element.lineNumber
                }
            })
        }
    }

    private fun setupComponent() {
        appComponent = DaggerAppComponent.builder()
                .networkModule(NetworkModule(this))
                .build()
    }

}