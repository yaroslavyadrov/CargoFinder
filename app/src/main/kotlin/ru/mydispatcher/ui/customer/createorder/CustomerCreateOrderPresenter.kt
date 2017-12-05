package ru.mydispatcher.ui.customer.createorder

import ru.mydispatcher.data.DataManager
import ru.mydispatcher.ui.base.BasePresenter
import javax.inject.Inject

class CustomerCreateOrderPresenter @Inject constructor(private val dataManager: DataManager) : BasePresenter<CustomerCreateOrderMvpView>() {
}