package com.example.yaroslavyadrov.cargofinder.ui.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.yaroslavyadrov.cargofinder.injection.component.ActivityComponent
import com.example.yaroslavyadrov.cargofinder.util.extensions.getAppComponent


abstract class BaseActivity : AppCompatActivity() {

    protected var activityComponent: ActivityComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        activityComponent = getAppComponent().activityComponent()
    }

    override fun onDestroy() {
        activityComponent = null
        super.onDestroy()
    }

    protected abstract fun getLayoutResId(): Int

}