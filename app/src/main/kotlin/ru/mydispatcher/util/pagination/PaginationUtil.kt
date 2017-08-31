package ru.mydispatcher.util.pagination


import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import java.util.*

object PaginationUtil {

    private val EMPTY_LIST_ITEMS_COUNT = 0
    private val DEFAULT_LIMIT = 30
    private val MAX_ATTEMPTS_TO_RETRY_LOADING = 3

    fun <T> paging(recyclerView: RecyclerView,
                   pagingListener: PagingListener<T>,
                   limit: Int = DEFAULT_LIMIT,
                   emptyListCount: Int = EMPTY_LIST_ITEMS_COUNT,
                   retryCount: Int = MAX_ATTEMPTS_TO_RETRY_LOADING): Observable<List<T>> {
        if (recyclerView.adapter == null) {
            throw PagingException("null recyclerView adapter")
        }
        if (limit <= 0) {
            throw PagingException("limit must be greater then 0")
        }
        if (emptyListCount < 0) {
            throw PagingException("emptyListCount must be not less then 0")
        }
        if (retryCount < 0) {
            throw PagingException("retryCount must be not less then 0")
        }

        val startNumberOfRetryAttempt = 0
        return getScrollObservable(recyclerView, limit, emptyListCount)
                .subscribeOn(AndroidSchedulers.mainThread())
                .distinctUntilChanged()
                .observeOn(Schedulers.newThread())
                .switchMap { offset -> getPagingObservable(recyclerView, pagingListener, pagingListener.onNextPage(offset), startNumberOfRetryAttempt, offset, retryCount) }
    }

    private fun getScrollObservable(recyclerView: RecyclerView, limit: Int, emptyListCount: Int): Observable<Int> {
        return Observable.create<Int> { subscriber ->
            val sl = object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (!subscriber.isDisposed) {
                        val position = getLastVisibleItemPosition(recyclerView)
                        val updatePosition = getRealItemCount(recyclerView) - 1 - limit / 2
                        if (position >= updatePosition) {
                            subscriber.onNext(getRealItemCount(recyclerView))
                        }
                    }
                }
            }
            recyclerView.addOnScrollListener(sl)
            subscriber.setDisposable(Disposables.fromAction { recyclerView.removeOnScrollListener(sl) })
            if (getRealItemCount(recyclerView) == emptyListCount) {
                subscriber.onNext(getRealItemCount(recyclerView))
            }
        }
    }

    private fun getLastVisibleItemPosition(recyclerView: RecyclerView): Int {
        val recyclerViewLMClass = recyclerView.layoutManager.javaClass
        if (recyclerViewLMClass == LinearLayoutManager::class.java || LinearLayoutManager::class.java.isAssignableFrom(recyclerViewLMClass)) {
            val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
            return linearLayoutManager.findLastVisibleItemPosition()
        } else if (recyclerViewLMClass == StaggeredGridLayoutManager::class.java || StaggeredGridLayoutManager::class.java.isAssignableFrom(recyclerViewLMClass)) {
            val staggeredGridLayoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager
            val intoList = staggeredGridLayoutManager.findLastVisibleItemPositions(null).toList()
            return Collections.max(intoList)
        }
        throw PagingException("Unknown LayoutManager class: " + recyclerViewLMClass.toString())
    }

    private fun <T> getPagingObservable(recyclerView: RecyclerView, listener: PagingListener<T>, observable: Observable<List<T>>, numberOfAttemptToRetry: Int, offset: Int, retryCount: Int): Observable<List<T>> {
        return observable
                .flatMap { ts ->
                    if (offset > 0 && ts.isEmpty()) {
                        if (recyclerView.adapter is PagingRecyclerViewAdapter<*, *>) {
                            (recyclerView.adapter as PagingRecyclerViewAdapter<*, *>).isAllItemsLoaded = true
                        }
                        return@flatMap Observable.create<List<T>> { it.onComplete() }
                    }
                    return@flatMap Observable.create<List<T>> { it.onNext(ts) }
                }
                .onErrorResumeNext { error: Throwable ->
                    // retry to load new data portion if error occurred
                    if (numberOfAttemptToRetry < retryCount) {
                        val attemptToRetryInc = numberOfAttemptToRetry + 1
                        getPagingObservable(recyclerView, listener, listener.onNextPage(offset), attemptToRetryInc, offset, retryCount)
                    } else {
                        if (offset == 0) {
                            Observable.error(error)
                        } else {
                            Observable.empty()
                        }
                    }
                }
    }

    private fun getRealItemCount(recyclerView: RecyclerView): Int {
        if (recyclerView.adapter is PagingRecyclerViewAdapter<*, *>) {
            return (recyclerView.adapter as PagingRecyclerViewAdapter<*, *>).realItemCount
        } else {
            return recyclerView.adapter.itemCount
        }
    }
}
