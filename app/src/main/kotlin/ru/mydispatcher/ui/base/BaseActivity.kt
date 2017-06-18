package ru.mydispatcher.ui.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ru.mydispatcher.R
import ru.mydispatcher.injection.component.ActivityComponent
import ru.mydispatcher.util.extensions.getAppComponent
import org.jetbrains.anko.indeterminateProgressDialog

abstract class BaseActivity : AppCompatActivity() {

    protected var activityComponent: ActivityComponent? = null

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

    protected abstract fun getLayoutResId(): Int

    protected fun showProgressAlert() = progressDialog.value.show()

    protected fun hideProgressAlert() = progressDialog.value.dismiss()
}