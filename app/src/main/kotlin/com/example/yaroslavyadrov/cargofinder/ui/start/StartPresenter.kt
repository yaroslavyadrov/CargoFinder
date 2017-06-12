package com.example.yaroslavyadrov.cargofinder.ui.start

import com.example.yaroslavyadrov.cargofinder.data.DataManager
import com.example.yaroslavyadrov.cargofinder.data.model.CargoFinderException
import com.example.yaroslavyadrov.cargofinder.ui.base.BasePresenter
import com.example.yaroslavyadrov.cargofinder.util.UserType
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class StartPresenter @Inject constructor(private val dataManager: DataManager) : BasePresenter<StartView>() {

    fun showUserTypeDialog() = view?.showUserTypeDialog()
    fun openDriverRegistration() = view?.openDriverRegistration()
    fun openCustomerRegistration() = view?.openCustomerRegistration()

    fun getGuestToken(userType: UserType, uid: String) {
        view?.showProgressDialog()
        dataManager.getGuestToken(userType, uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEvent { view?.hideProgressDialog() }
                .subscribe(
                        {
                            //                            view?.openEnterRouteActivity(userType.type)
                        },
                        { error ->
                            Timber.e(error)
                            when (error.cause) {
                                is CargoFinderException -> view?.showError((error.cause as CargoFinderException).message)
                            }
                        })
    }
}