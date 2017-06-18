package com.example.yaroslavyadrov.cargofinder.ui.start

import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AlertDialog
import com.example.yaroslavyadrov.cargofinder.R
import com.example.yaroslavyadrov.cargofinder.ui.base.BaseActivity
import com.example.yaroslavyadrov.cargofinder.ui.checkcode.CheckCodeActivity
import com.example.yaroslavyadrov.cargofinder.util.EXTRA_PHONE_CODE
import com.example.yaroslavyadrov.cargofinder.util.EXTRA_PHONE_NUMBER
import com.example.yaroslavyadrov.cargofinder.util.extensions.addPhoneTextWatcher
import com.example.yaroslavyadrov.cargofinder.util.extensions.showSnackbar
import kotlinx.android.synthetic.main.layout_activity_start.*
import org.jetbrains.anko.startActivity
import timber.log.Timber
import javax.inject.Inject


class StartActivity : BaseActivity(), StartView {

    @Inject lateinit var presenter: StartPresenter

    private lateinit var userTypeDialog: AlertDialog

    override fun getLayoutResId() = R.layout.layout_activity_start

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent?.inject(this)
        presenter.bind(this)
        editTextPhone.addPhoneTextWatcher()
        userTypeDialog = createUserTypeDialog()
        buttonRegister.setOnClickListener { presenter.showUserTypeDialog() }
        val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        buttonEnter.setOnClickListener {
            val phone = editTextPhone.text.filter { it.isDigit() }.toString()
            presenter.attemptLogin(textViewCode.text.toString(), phone, deviceId)
        }
    }

    override fun onDestroy() {
        presenter.destroy()
        if (userTypeDialog.isShowing) {
            userTypeDialog.dismiss()
        }
        super.onDestroy()
    }

    private fun createUserTypeDialog(): AlertDialog =
            AlertDialog.Builder(this).apply {
                val userTypes = arrayOf<CharSequence>(getString(R.string.driver), getString(R.string.customer))
                setTitle(getString(R.string.who_are_you))
                setItems(userTypes, { _, which ->
                    when (userTypes[which]) {
                        getString(R.string.driver) -> presenter.openDriverRegistration()
                        getString(R.string.customer) -> presenter.openCustomerRegistration()
                    }
                })
            }.create()


    override fun showUserTypeDialog() {
        userTypeDialog.show()
    }

    override fun openCheckCodeActivity(code: String, phone: String) {
        startActivity<CheckCodeActivity>(EXTRA_PHONE_CODE to code, EXTRA_PHONE_NUMBER to phone)
    }

    override fun openDriverRegistration() {
        Timber.d("driver")
    }

    override fun openCustomerRegistration() {
        Timber.d("customer")
    }

    override fun showProgressDialog() = showProgressAlert()


    override fun hideProgressDialog() = hideProgressAlert()


    override fun showError(message: String) = showSnackbar(buttonRegister, message)

    override fun showError(messageRes: Int) = showSnackbar(buttonRegister, getString(messageRes))
}

