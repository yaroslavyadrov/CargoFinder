package ru.mydispatcher.ui.customer.orders.orderslist

import ru.mydispatcher.data.DataManager
import ru.mydispatcher.ui.base.BasePresenter
import javax.inject.Inject


class OrderListPresenter @Inject constructor(private val dataManager: DataManager) : BasePresenter<OrderListMvpView>() {
    fun getOrders(showActiveOrders: Boolean) {

    }


}
