package ru.mydispatcher.ui.customer.orders.orderslist

import android.support.v7.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.mydispatcher.R
import ru.mydispatcher.data.DataManager
import ru.mydispatcher.data.model.CargoFinderException
import ru.mydispatcher.data.model.CustomerOrder
import ru.mydispatcher.ui.base.BasePresenter
import ru.mydispatcher.util.pagination.PaginationUtil
import ru.mydispatcher.util.pagination.PagingListener
import timber.log.Timber
import javax.inject.Inject


class OrderListPresenter @Inject constructor(private val dataManager: DataManager) : BasePresenter<OrderListMvpView>() {

    private var getOrders: Disposable? = null

    fun onScreenOpen(
            showActiveOrders: Boolean,
            recyclerView: RecyclerView
    ) {
        val type = if (showActiveOrders) "active" else "inactive"
        getOrders?.dispose()
        view?.showLoading()
        getOrders = PaginationUtil.paging(
                recyclerView,
                object : PagingListener<CustomerOrder> {
                    override fun onNextPage(offset: Int) = dataManager.getCustomerOrders(type, offset)
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            view?.showData()
                            when (it.size) {
                                0 -> view?.showEmpty()
                                else -> view?.addItems(it)
                            }
                        },
                        { error ->
                            Timber.e(error)
                            view?.showError()
                            when (error.cause) {
                                is CargoFinderException -> view?.showErrorMessage((error.cause as CargoFinderException).message)
                                else -> view?.showErrorMessage(R.string.unexpected_error)
                            }
                        })
    }

    override fun destroy() {
        getOrders?.dispose()
        super.destroy()
    }
}
