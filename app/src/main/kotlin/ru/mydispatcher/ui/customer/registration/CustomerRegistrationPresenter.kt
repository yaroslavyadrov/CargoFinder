package ru.mydispatcher.ui.customer.registration

import android.net.Uri
import com.kbeanie.multipicker.api.entity.ChosenImage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.RequestBody
import ru.mydispatcher.R
import ru.mydispatcher.data.DataManager
import ru.mydispatcher.data.model.CargoFinderException
import ru.mydispatcher.data.model.GeoObject
import ru.mydispatcher.ui.base.BasePresenter
import ru.mydispatcher.util.extensions.onlyDigits
import ru.mydispatcher.util.extensions.toRequestBody
import timber.log.Timber
import java.io.File
import javax.inject.Inject


class CustomerRegistrationPresenter @Inject constructor(private val dataManager: DataManager) : BasePresenter<CustomerRegistrationMvpView>() {
    var photo: Uri? = null
    var cityId: Int? = null

    fun registrationAttempt(name: String, organization: String, phone: String) {
        view?.showProgressDialog()
        val params = mutableMapOf<String, RequestBody>()
        params["name"] = name.toRequestBody()
        if (!organization.isEmpty()) params["organization"] = organization.toRequestBody()
        params["phone"] = phone.onlyDigits().toRequestBody()
        params["country_id"] = "1".toRequestBody()
        params["city_id"] = cityId.toString().toRequestBody()
        photo?.let {
            params["avatar"] = File(photo?.path).toRequestBody()
        }
        Timber.d(params.toString())
        disposables.add(dataManager.registerCustomer(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEvent { view?.hideProgressDialog() }
                .subscribe(
                        {
                            view?.openCheckCodeActivity("+7", phone.onlyDigits())
                        },
                        { error ->
                            Timber.e(error)
                            when (error.cause) {
                                is CargoFinderException -> view?.showError((error.cause as CargoFinderException).message)
                                else -> view?.showError(R.string.unexpected_error)
                            }
                        }))
    }

    fun showGeoObjectsDialog() = view?.showGeoObjectsDialog()

    fun geoObjectSelected(geoObject: GeoObject) {
        cityId = geoObject.id
        view?.geoObjectSelected(geoObject)
    }

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
