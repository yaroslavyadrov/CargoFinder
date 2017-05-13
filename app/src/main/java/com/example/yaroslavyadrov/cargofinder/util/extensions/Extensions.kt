package com.example.yaroslavyadrov.cargofinder.util.extensions

import android.content.Context
import com.example.yaroslavyadrov.cargofinder.CargoFinderApplication
import com.example.yaroslavyadrov.cargofinder.injection.component.ApplicationComponent

fun Context.getAppComponent(): ApplicationComponent = (applicationContext as CargoFinderApplication).appComponent
