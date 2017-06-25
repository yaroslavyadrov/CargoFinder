package ru.mydispatcher.util.pagination

import io.reactivex.Observable


interface PagingListener<T> {
    fun onNextPage(offset: Int): Observable<List<T>>
}
