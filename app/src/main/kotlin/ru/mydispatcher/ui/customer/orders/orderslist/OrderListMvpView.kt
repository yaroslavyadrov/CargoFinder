package ru.mydispatcher.ui.customer.orders.orderslist

import ru.mydispatcher.data.model.CustomerOrder
import ru.mydispatcher.ui.base.MvpView


interface OrderListMvpView : MvpView {
    fun showOrders(orders: List<CustomerOrder>)
}
