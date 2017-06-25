package ru.mydispatcher.ui.start

import ru.mydispatcher.R
import ru.mydispatcher.data.DataManager
import ru.mydispatcher.data.model.CargoFinderException
import ru.mydispatcher.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.*
import ru.mydispatcher.ui.base.MvpView
import timber.log.Timber
import javax.inject.Inject

class StartPresenter @Inject constructor(private val dataManager: DataManager) : BasePresenter<StartView>() {

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
                                    view?.openCheckCodeActivity(code, phone)
                                },
                                { error ->
                                    Timber.e(error)
                                    when (error.cause) {
                                        is CargoFinderException -> view?.showError((error.cause as CargoFinderException).message)
                                        else -> view?.showError(R.string.unexpected_error)
                                    }
                                }))
            }
        }
    }
}