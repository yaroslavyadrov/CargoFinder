package com.example.yaroslavyadrov.cargofinder.util.extensions

import android.content.Context
import android.support.design.widget.Snackbar
import android.view.View
import com.example.yaroslavyadrov.cargofinder.CargoFinderApplication
import com.example.yaroslavyadrov.cargofinder.injection.component.AppComponent

fun Context.getAppComponent(): AppComponent = (applicationContext as CargoFinderApplication).appComponent

fun showSnackbar(view: View, text: String) {
    Snackbar.make(view, text, Snackbar.LENGTH_LONG).show()
}