package ru.mydispatcher.ui.selectgeoobject

import android.support.annotation.StringRes
import ru.mydispatcher.data.model.GeoObject
import ru.mydispatcher.ui.base.MvpView


interface SelectGeoObjectView : MvpView {
    fun showLoading()
    fun addItems(geoObjects: List<GeoObject>)
    fun showError(message: String)
    fun showError(@StringRes message: Int)
    fun showEmpty()
}