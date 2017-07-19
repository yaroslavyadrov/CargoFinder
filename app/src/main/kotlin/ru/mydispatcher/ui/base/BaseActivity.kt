package ru.mydispatcher.ui.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.indeterminateProgressDialog
import ru.mydispatcher.R
import ru.mydispatcher.injection.component.ActivityComponent
import ru.mydispatcher.ui.base.BaseActivity.Companion.REQUEST_CODE
import ru.mydispatcher.util.extensions.getAppComponent

abstract class BaseActivity : AppCompatActivity() {

    var activityComponent: ActivityComponent? = null
    val eventActivityResult = ArrayList<(Intent) -> Unit>()

    companion object {
        val REQUEST_CODE = 100
    }

    protected val progressDialog = lazy {
        indeterminateProgressDialog(message = R.string.loading).apply { setCancelable(false) }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE && data != null) {
            eventActivityResult.forEach { it(data) }
        }
        eventActivityResult.clear()
    }


    protected abstract fun getLayoutResId(): Int

    protected fun showProgressAlert() = progressDialog.value.show()

    protected fun hideProgressAlert() = progressDialog.value.dismiss()
}

data class Result(val resultCode: Int, val data: Intent?)

fun BaseActivity.waitForActivityResult(intent: Intent, callback: (Intent) -> Unit) {
    eventActivityResult.add(callback)
    startActivityForResult(intent, REQUEST_CODE)
}