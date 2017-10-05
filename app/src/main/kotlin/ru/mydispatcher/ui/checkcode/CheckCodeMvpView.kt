package ru.mydispatcher.ui.checkcode

import android.support.annotation.StringRes
import ru.mydispatcher.ui.base.MvpView;

interface CheckCodeMvpView : MvpView {
    fun showDetails(phoneNumber: String)
    fun showRemain(seconds: Int)
    fun showResend()
    fun showProgress()
    fun hideProgress()
    fun showError(message: String)
    fun showError(@StringRes messageRes: Int)
    fun openCustomerStartActivity()
}
