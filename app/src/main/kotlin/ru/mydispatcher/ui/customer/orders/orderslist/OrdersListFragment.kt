package ru.mydispatcher.ui.customer.orders.orderslist

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_orders.*
import ru.mydispatcher.R
import ru.mydispatcher.data.model.CustomerOrder
import ru.mydispatcher.ui.base.BaseActivity
import javax.inject.Inject


class OrdersListFragment :
        Fragment(),
        OrderListMvpView {

    @Inject lateinit var presenter: OrderListPresenter
    @Inject lateinit var ordersAdapter: OrdersAdapter
    private val showActiveOrders by lazy { arguments.getBoolean(ACTIVE_ORDERS) }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_orders, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as BaseActivity).activityComponent?.inject(this)
        presenter.bind(this)
        with(recyclerViewOrders) {
            layoutManager = LinearLayoutManager(activity)
            adapter = ordersAdapter
        }
        presenter.getOrders(showActiveOrders)
    }

    override fun onDestroyView() {
        presenter.destroy()
        super.onDestroyView()
    }

    override fun showOrders(orders: List<CustomerOrder>) {
        ordersAdapter.items = orders
    }

    companion object {
        private val ACTIVE_ORDERS = "extras.active_orders"
        fun createFragment(activeOrders: Boolean): OrdersListFragment {
            val bundle = Bundle()
            bundle.putBoolean(ACTIVE_ORDERS, activeOrders)
            val fragment = OrdersListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}