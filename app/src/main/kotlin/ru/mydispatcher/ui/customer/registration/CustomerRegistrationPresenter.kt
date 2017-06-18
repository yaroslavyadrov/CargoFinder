package ru.mydispatcher.ui.customer.registration

import ru.mydispatcher.data.DataManager
import ru.mydispatcher.ui.base.BasePresenter
import javax.inject.Inject;


class CustomerRegistrationPresenter @Inject constructor(private val dataManager: DataManager) : BasePresenter<CustomerRegistrationMvpView>() {

}
