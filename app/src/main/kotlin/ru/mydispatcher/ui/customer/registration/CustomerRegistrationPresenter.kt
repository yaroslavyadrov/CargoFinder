package ru.mydispatcher.ui.customer.registration

import android.net.Uri
import com.kbeanie.multipicker.api.entity.ChosenImage
import ru.mydispatcher.data.DataManager
import ru.mydispatcher.data.model.GeoObject
import ru.mydispatcher.ui.base.BasePresenter
import java.io.File
import javax.inject.Inject;


class CustomerRegistrationPresenter @Inject constructor(private val dataManager: DataManager) : BasePresenter<CustomerRegistrationMvpView>() {
    var photo: Uri? = null

    fun registrationAttempt(name: String, organization: String, phone: String, cityId: Int) {

    }

    fun showGeoObjectsDialog() = view?.showGeoObjectsDialog()

    fun geoObjectSelected(geoObject: GeoObject) = view?.geoObjectSelected(geoObject)

    fun showPopupMenu() {
        view?.showPopupMenu(photo != null)
    }

    fun photoSelected(photo: ChosenImage) {
        view?.cropPhoto(Uri.fromFile(File(photo.originalPath)))
    }

    fun photoCropped(uri: Uri) {
        photo = uri
        view?.showSelectedAvatar(uri)
    }

    fun takePhoto() = view?.takePhoto()


    fun chooseFromGallery() = view?.chooseFromGallery()


    fun deletePhoto() {
        photo = null
        view?.deletePhoto()
    }

}
