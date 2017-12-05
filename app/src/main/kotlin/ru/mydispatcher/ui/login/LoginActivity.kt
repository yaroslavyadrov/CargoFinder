package ru.mydispatcher.ui.login

import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import org.jetbrains.anko.startActivity
import ru.mydispatcher.R
import ru.mydispatcher.ui.base.BaseActivity
import ru.mydispatcher.ui.checkcode.CheckCodeActivity
import ru.mydispatcher.ui.customer.registration.CustomerRegistrationActivity
import ru.mydispatcher.util.EXTRA_PHONE_CODE
import ru.mydispatcher.util.EXTRA_PHONE_NUMBER
import ru.mydispatcher.util.extensions.addPhoneTextWatcher
import ru.mydispatcher.util.extensions.bindView
import ru.mydispatcher.util.extensions.onlyDigits
import ru.mydispatcher.util.extensions.showSnackbar
import timber.log.Timber
import javax.inject.Inject


class LoginActivity : BaseActivity(), LoginView {

    @Inject lateinit var presenter: LoginPresenter

    private val editTextPhone by bindView<EditText>(R.id.editTextPhone)
    private val textViewCode by bindView<TextView>(R.id.textViewCode)
    private val buttonRegister by bindView<Button>(R.id.buttonRegister)
    private val buttonEnter by bindView<Button>(R.id.buttonEnter)

    private lateinit var userTypeDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_activity_login)
        activityComponent?.inject(this)
        presenter.bind(this)
        editTextPhone.addPhoneTextWatcher()
        userTypeDialog = createUserTypeDialog()
        buttonRegister.setOnClickListener { presenter.showUserTypeDialog() }
        val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        buttonEnter.setOnClickListener {
            val phone = editTextPhone.text.toString().onlyDigits()
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
        startActivity<CustomerRegistrationActivity>()
    }

    override fun showProgressDialog() = showProgressAlert()


    override fun hideProgressDialog() = hideProgressAlert()


    override fun showError(message: String) = showSnackbar(buttonRegister, message)

    override fun showError(messageRes: Int) = showSnackbar(buttonRegister, getString(messageRes))
}

