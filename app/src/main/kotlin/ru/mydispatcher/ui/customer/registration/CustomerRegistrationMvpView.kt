package ru.mydispatcher.ui.customer.registration

import android.net.Uri
import ru.mydispatcher.data.model.GeoObject
import ru.mydispatcher.ui.base.MvpView

interface CustomerRegistrationMvpView : MvpView {
    //    fun showNameError(@StringRes error: Int)
//    fun showPhoneError(@StringRes error: Int)
//    fun showCityError(@StringRes error: Int)
    fun showProgressDialog()
    fun hideProgressDialog()
    fun showGeoObjectsDialog()
    fun geoObjectSelected(geoObject: GeoObject)
    fun showPopupMenu(showDelete: Boolean)
    fun takePhoto()
    fun chooseFromGallery()
    fun deletePhoto()
    fun cropPhoto(uri: Uri)
    fun showSelectedAvatar(uri: Uri)
}
