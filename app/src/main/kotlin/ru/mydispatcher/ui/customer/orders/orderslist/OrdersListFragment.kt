package ru.mydispatcher.ui.customer.orders.orderslist

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import ru.mydispatcher.R
import ru.mydispatcher.data.model.CustomerOrder
import ru.mydispatcher.ui.base.BaseActivity
import ru.mydispatcher.util.extensions.bindView
import ru.mydispatcher.util.extensions.hide
import ru.mydispatcher.util.extensions.show
import javax.inject.Inject


class OrdersListFragment :
        Fragment(),
        OrderListMvpView {
    private val recyclerViewOrders by bindView<RecyclerView>(R.id.recyclerViewOrders)
    override val viewData by lazy { recyclerViewOrders }
    override val viewError by bindView<View>(R.id.viewError)
    override val viewLoading by bindView<View>(R.id.viewLoading)
    override val buttonRetry by bindView<Button>(R.id.buttonRetry)
    override val textViewErrorMessage by bindView<TextView>(R.id.textViewErrorMessage)
    private val viewEmpty by bindView<View>(R.id.viewEmpty)

    @Inject lateinit var presenter: OrderListPresenter
    @Inject lateinit var ordersAdapter: OrdersAdapter
    private val showActiveOrders by lazy { arguments.getBoolean(ACTIVE_ORDERS) }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.fragment_orders, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as BaseActivity).activityComponent?.inject(this)
        presenter.bind(this)
        with(recyclerViewOrders) {
            layoutManager = LinearLayoutManager(activity)
            adapter = ordersAdapter
        }
        presenter.onScreenOpen(showActiveOrders, recyclerViewOrders)
        buttonRetry.setOnClickListener { presenter.onScreenOpen(showActiveOrders, recyclerViewOrders) }
    }

    override fun onDestroyView() {
        presenter.destroy()
        super.onDestroyView()
    }

    override fun showData() {
        super.showData()
        viewEmpty.hide()
    }

    override fun showLoading() {
        super.showLoading()
        viewEmpty.hide()
    }

    override fun showError() {
        super.showError()
        viewEmpty.hide()
    }

    override fun showEmpty() {
        viewEmpty.show()
        viewData.hide()
        viewLoading.hide()
        viewError.hide()
    }

    override fun addItems(orders: List<CustomerOrder>) {
        ordersAdapter.addItems(orders)
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