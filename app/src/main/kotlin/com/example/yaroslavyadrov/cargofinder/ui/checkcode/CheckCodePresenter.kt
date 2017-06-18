package com.example.yaroslavyadrov.cargofinder.ui.checkcode;

import com.example.yaroslavyadrov.cargofinder.R
import com.example.yaroslavyadrov.cargofinder.data.DataManager;
import com.example.yaroslavyadrov.cargofinder.data.model.CargoFinderException
import com.example.yaroslavyadrov.cargofinder.ui.base.BasePresenter;
import com.example.yaroslavyadrov.cargofinder.util.UserType
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import org.joda.time.Duration
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject;


class CheckCodePresenter @Inject constructor(private val dataManager: DataManager) : BasePresenter<CheckCodeMvpView>() {
    fun showPhoneNumberDetails(code: String, phone: String) {
        val number = "$code (${phone.subSequence(0, 3)}) ${phone.subSequence(3, 6)}-${phone.subSequence(6, 8)}-${phone.subSequence(8, 10)}"
        view?.showDetails(number)
    }

    fun sendCode(code: String, phone: String) {
        Timber.d("send code")
        val previousSms = dataManager.getPreviousDateOfSms()
        val timeSincePreviousSms = Duration(previousSms, DateTime())
        when (timeSincePreviousSms.standardSeconds) {
            in 0..120 -> showTimer(120 - timeSincePreviousSms.standardSeconds)
            else -> {
                view?.showProgress()
                disposables.add(dataManager.sendCode(code, phone)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnEvent { view?.hideProgress() }
                        .subscribe(
                                {
                                    showTimer(120)
                                    dataManager.setPreviousDateOfSms(DateTime())
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
    }

    fun showTimer(seconds: Long) {
        val start = seconds
        disposables.add(Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .take(seconds)
                .map { passed -> start.toInt() - passed.toInt() }
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
                                UserType.CUSTOMER -> Timber.d("customer")
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
