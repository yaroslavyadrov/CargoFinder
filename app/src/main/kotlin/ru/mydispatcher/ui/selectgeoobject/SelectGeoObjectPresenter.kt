package ru.mydispatcher.ui.selectgeoobject

import android.support.v7.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.mydispatcher.R
import ru.mydispatcher.data.DataManager
import ru.mydispatcher.data.model.CargoFinderException
import ru.mydispatcher.data.model.GeoObject
import ru.mydispatcher.ui.base.BasePresenter
import ru.mydispatcher.util.pagination.PaginationUtil
import ru.mydispatcher.util.pagination.PagingListener
import timber.log.Timber
import javax.inject.Inject

class SelectGeoObjectPresenter @Inject constructor(private val dataManager: DataManager) : BasePresenter<SelectGeoObjectView>() {
    var searchObjects: Disposable? = null

    override fun destroy() {
        searchObjects?.apply { dispose() }
        super.destroy()
    }

    fun getObjects(recyclerView: RecyclerView, geoObjectType: String, query: String) {
        searchObjects?.apply { dispose() }
        view?.showLoading()
        searchObjects = PaginationUtil.paging(
                recyclerView,
                object : PagingListener<GeoObject> {
                    override fun onNextPage(offset: Int): Observable<List<GeoObject>> {
                        return dataManager.getGeoObjects(query, geoObjectType, offset)
                    }
                }
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            when (it.size) {
                                0 -> view?.showEmpty()
                                else -> view?.addItems(it)
                            }
                        },
                        { error ->
                            Timber.e(error)
                            when (error.cause) {
                                is CargoFinderException -> view?.showError((error.cause as CargoFinderException).message)
                                else -> view?.showError(R.string.unexpected_error)
                            }
                        })
    }
}
