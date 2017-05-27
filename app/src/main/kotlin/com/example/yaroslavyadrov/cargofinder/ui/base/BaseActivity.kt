package com.example.yaroslavyadrov.cargofinder.ui.base

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.yaroslavyadrov.cargofinder.R
import com.example.yaroslavyadrov.cargofinder.injection.component.ActivityComponent
import com.example.yaroslavyadrov.cargofinder.util.extensions.getAppComponent
import com.example.yaroslavyadrov.cargofinder.util.extensions.showSnackbar


abstract class BaseActivity : AppCompatActivity() {

    protected var activityComponent: ActivityComponent? = null

    protected val progressDialog = lazy {
        AlertDialog.Builder(this)
                .setView(R.layout.view_progress_dialog)
                .setCancelable(false)
                .create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        activityComponent = getAppComponent().activityComponent()
    }

    override fun onDestroy() {
        activityComponent = null
        super.onDestroy()
        with(progressDialog) {
            if (isInitialized() && value.isShowing) {
                value.dismiss()
            }
        }
    }

    protected abstract fun getLayoutResId(): Int

    protected fun showProgressAlert() = progressDialog.value.show()

    protected fun hideProgressAlert() = progressDialog.value.dismiss()
}