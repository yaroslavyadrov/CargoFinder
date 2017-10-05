package ru.mydispatcher.ui.checkcode;

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import org.joda.time.Duration
import ru.mydispatcher.R
import ru.mydispatcher.data.DataManager
import ru.mydispatcher.data.model.CargoFinderException
import ru.mydispatcher.ui.base.BasePresenter
import ru.mydispatcher.util.UserType
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class CheckCodePresenter @Inject constructor(private val dataManager: DataManager) : BasePresenter<CheckCodeMvpView>() {

    fun showPhoneNumberDetails(code: String, phone: String) {
        val number = "$code (${phone.subSequence(0, 3)}) ${phone.subSequence(3, 6)}-${phone.subSequence(6, 8)}-${phone.subSequence(8, 10)}"
        view?.showDetails(number)
    }

    fun onScreenOpen() {
        val previousSms = dataManager.previousSmsTime
        val timeSincePreviousSms = Duration(previousSms, DateTime())
        when (timeSincePreviousSms.standardSeconds) {
            in 0..120 -> showTimer(120 - timeSincePreviousSms.standardSeconds)
            else -> showTimer(120)
        }
    }

    fun sendCode(code: String, phone: String) {
        view?.showProgress()
        disposables.add(dataManager.sendCode(code, phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEvent { view?.hideProgress() }
                .subscribe(
                        {
                            showTimer(120)
                            dataManager.previousSmsTime = DateTime()
                        },
                        { error ->
                            Timber.e(error)
                            when (error.cause) {
                                is CargoFinderException -> view?.showError((error.cause as CargoFinderException).message)
                                else -> view?.showError(R.string.unexpected_error)
                            }
                        }))
    }


    private fun showTimer(seconds: Long) {
        disposables.add(Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .take(seconds)
                .map { passed -> seconds.toInt() - passed.toInt() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { remain -> view?.showRemain(remain) },
                        {},
                        { view?.showResend() }))
    }

    fun checkCode(code: String) {
        view?.showProgress()
        disposables.add(dataManager.checkCode(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEvent { _, _ -> view?.hideProgress() }
                .subscribe(
                        {
                            when (it) {
                                UserType.DRIVER -> Timber.d("driver")
                                UserType.CUSTOMER -> view?.openCustomerStartActivity()
                            }
                        },
                        { error ->
                            Timber.e(error)
                            when (error.cause) {
                                is CargoFinderException -> view?.showError((error.cause as CargoFinderException).message)
                                else -> view?.showError(R.string.unexpected_error)
                            }
                        })
        )
    }

}
