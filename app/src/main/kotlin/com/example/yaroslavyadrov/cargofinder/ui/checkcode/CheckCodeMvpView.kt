package com.example.yaroslavyadrov.cargofinder.ui.checkcode;

import android.support.annotation.StringRes
import com.example.yaroslavyadrov.cargofinder.ui.base.MvpView;

interface CheckCodeMvpView : MvpView {
    fun showDetails(phoneNumber: String)
    fun showRemain(seconds: Int)
    fun showResend()
    fun showProgress()
    fun hideProgress()
    fun showError(message: String)
    fun showError(@StringRes messageRes: Int)
}
