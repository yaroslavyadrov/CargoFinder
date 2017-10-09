package ru.mydispatcher.ui.customer.orders.orderslist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.mydispatcher.R
import ru.mydispatcher.data.model.CustomerOrder
import javax.inject.Inject


class OrdersAdapter @Inject constructor() : RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {
    var items: MutableList<CustomerOrder> = mutableListOf()

    private var onItemClick: (CustomerOrder) -> Unit = {}

    fun onItemClick(listener: (CustomerOrder) -> Unit) {
        onItemClick = listener
    }

    fun getItem(position: Int) = items[position]

    fun addItems(orders: List<CustomerOrder>) {
        val lastPosition = items.size - 1
        items.addAll(orders)
        if (lastPosition >= 0) {
            notifyItemRangeInserted(lastPosition, orders.size)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_order, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    override fun getItemCount() = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(order: CustomerOrder) {
            with(order) {
            }
        }
    }
}

