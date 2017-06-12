package com.example.yaroslavyadrov.cargofinder

import android.app.Application
import android.net.Uri
import android.support.multidex.MultiDexApplication
import android.util.Log
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
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return super.createStackElementTag(element) + ':' + element.lineNumber
                }
            })
        }
        if (bugseeEnabled()) {
            Log.d("TAGG", "enabled")
        } else {
            Log.d("TAGG", "disabled")
        }
    }

    private fun setupComponent() {
        appComponent = DaggerAppComponent.builder()
                .networkModule(NetworkModule(this))
                .build()
    }

}


fun Application.bugseeEnabled(): Boolean {
    var bugseeEnabled = false
    val cr = contentResolver
    val cursor = cr.query(Uri.parse("content://ru.handh.abs.allowed"), null, null, null, null) ?: return false
    cursor.use {
        val index = cursor.getColumnIndex("bugsee")
        if (index != -1) {
            cursor.moveToFirst()
            bugseeEnabled = cursor.getString(index).toBoolean()
        }
    }
    return bugseeEnabled
}