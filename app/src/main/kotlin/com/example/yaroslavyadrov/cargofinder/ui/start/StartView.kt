package com.example.yaroslavyadrov.cargofinder.ui.start

import android.support.annotation.StringRes
import com.example.yaroslavyadrov.cargofinder.ui.base.MvpView


interface StartView : MvpView {

    fun showUserTypeDialog()
    fun openDriverRegistration()
    fun openCustomerRegistration()
    fun openCheckCodeActivity(code: String, phone: String)
    fun showProgressDialog()
    fun hideProgressDialog()
    fun showError(message: String)
    fun showError(@StringRes messageRes: Int)

}