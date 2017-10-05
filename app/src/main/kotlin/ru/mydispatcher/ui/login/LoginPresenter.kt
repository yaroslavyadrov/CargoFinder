package ru.mydispatcher.ui.login

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.mydispatcher.R
import ru.mydispatcher.data.DataManager
import ru.mydispatcher.data.model.CargoFinderException
import ru.mydispatcher.ui.base.BasePresenter
import timber.log.Timber
import javax.inject.Inject

class LoginPresenter @Inject constructor(private val dataManager: DataManager) : BasePresenter<LoginView>() {

    fun showUserTypeDialog() = view?.showUserTypeDialog()
    fun openDriverRegistration() = view?.openDriverRegistration()
    fun openCustomerRegistration() = view?.openCustomerRegistration()

    fun attemptLogin(code: String, phone: String, deviceId: String) {
        when (phone.length) {
            in 0..9 -> view?.showError(R.string.error_wrong_number)
            else -> {
                view?.showProgressDialog()
                disposables.add(dataManager.getGuestToken(deviceId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnEvent { view?.hideProgressDialog() }
                        .subscribe(
                                {
                                    requestCode(code, phone)
                                },
                                this::handleError))
            }
        }
    }

    private fun handleError(error: Throwable) {
        Timber.e(error)
        when (error.cause) {
            is CargoFinderException -> view?.showError((error.cause as CargoFinderException).message)
            else -> view?.showError(R.string.unexpected_error)
        }
    }

    private fun requestCode(code: String, phone: String) {
        disposables.add(dataManager.sendCode(code, phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            view?.openCheckCodeActivity(code, phone)
                        },
                        this::handleError))
    }
}