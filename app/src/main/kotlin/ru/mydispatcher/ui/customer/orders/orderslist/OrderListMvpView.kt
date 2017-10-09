package ru.mydispatcher.ui.customer.orders.orderslist

import ru.mydispatcher.data.model.CustomerOrder
import ru.mydispatcher.ui.base.MvpView
import ru.mydispatcher.ui.common.LoadingManager


interface OrderListMvpView : MvpView, LoadingManager {
    fun showEmpty()
    fun addItems(orders: List<CustomerOrder>)
}
