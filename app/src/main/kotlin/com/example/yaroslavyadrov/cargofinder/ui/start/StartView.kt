package com.example.yaroslavyadrov.cargofinder.ui.start

import com.example.yaroslavyadrov.cargofinder.ui.base.MvpView


interface StartView : MvpView {

    fun openCustomerAuthActivity()
    fun openDriverAuthActivity()
    fun showProgressDialog()
    fun hideProgressDialog()
    fun showError(message: String)

}