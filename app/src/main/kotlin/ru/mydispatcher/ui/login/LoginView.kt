package ru.mydispatcher.ui.login

import android.support.annotation.StringRes
import ru.mydispatcher.ui.base.MvpView


interface LoginView : MvpView {

    fun showUserTypeDialog()
    fun openDriverRegistration()
    fun openCustomerRegistration()
    fun openCheckCodeActivity(code: String, phone: String)
    fun showProgressDialog()
    fun hideProgressDialog()
    fun showError(message: String)
    fun showError(@StringRes messageRes: Int)

}