package ru.mydispatcher.ui.customer.registration


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.annotation.StringRes
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.jakewharton.rxbinding2.widget.textChanges
import com.kbeanie.multipicker.api.CameraImagePicker
import com.kbeanie.multipicker.api.ImagePicker
import com.kbeanie.multipicker.api.Picker
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback
import com.kbeanie.multipicker.api.entity.ChosenImage
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.functions.Function3
import io.reactivex.subscribers.DisposableSubscriber
import org.jetbrains.anko.startActivity
import ru.mydispatcher.R
import ru.mydispatcher.data.model.GeoObject
import ru.mydispatcher.ui.base.BaseActivity
import ru.mydispatcher.ui.base.waitForActivityResult
import ru.mydispatcher.ui.checkcode.CheckCodeActivity
import ru.mydispatcher.ui.imagecrop.ImageCropActivity
import ru.mydispatcher.ui.selectgeoobject.SelectGeoObjectDialog
import ru.mydispatcher.ui.selectgeoobject.SelectGeoObjectDialog.Companion.CITIES
import ru.mydispatcher.util.EXTRA_PHONE_CODE
import ru.mydispatcher.util.EXTRA_PHONE_NUMBER
import ru.mydispatcher.util.extensions.addPhoneTextWatcher
import ru.mydispatcher.util.extensions.bindView
import ru.mydispatcher.util.extensions.setBackArrowAndAction
import ru.mydispatcher.util.extensions.showSnackbar
import timber.log.Timber
import javax.inject.Inject


class CustomerRegistrationActivity :
        BaseActivity(),
        CustomerRegistrationMvpView {

    private val toolbar by bindView<Toolbar>(R.id.toolbar)
    private val imageViewEditAvatar by bindView<ImageView>(R.id.imageViewEditAvatar)
    private val editTextPhone by bindView<EditText>(R.id.editTextPhone)
    private val editTextCity by bindView<EditText>(R.id.editTextCity)
    private val editTextName by bindView<EditText>(R.id.editTextName)
    private val editTextOrganization by bindView<EditText>(R.id.editTextOrganization)
    private val buttonRegister by bindView<Button>(R.id.buttonRegister)
    private val imageViewAvatar by bindView<ImageView>(R.id.imageViewAvatar)


    @Inject lateinit var presenter: CustomerRegistrationPresenter

    lateinit var nameFlowable: Flowable<String>
    lateinit var phoneFlowable: Flowable<String>
    lateinit var cityFlowable: Flowable<String>
    private var isValidForm: Boolean = false
    lateinit private var disposableSubscriber: DisposableSubscriber<Boolean>
    private val cameraImagePicker: CameraImagePicker by lazy {
        CameraImagePicker(this).apply { setImagePickerCallback(imagePicker) }
    }
    private val internalStoragePhotoPicker: ImagePicker by lazy {
        ImagePicker(this).apply { setImagePickerCallback(imagePicker) }
    }
    val imagePicker: ImagePickerCallback by lazy {
        object : ImagePickerCallback {
            override fun onImagesChosen(images: MutableList<ChosenImage>?) {
                images?.let {
                    presenter.photoSelected(images[0])
                }
            }

            override fun onError(error: String?) {
                Timber.d(error)
            }
        }
    }
    private val rxPermissions: RxPermissions by lazy { RxPermissions(this) }
    private val popupMenu: PopupMenu by lazy {
        PopupMenu(this, imageViewEditAvatar).apply {
            inflate(R.menu.menu_edit_avatar)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_camera -> presenter.takePhoto()
                    R.id.action_gallery -> presenter.chooseFromGallery()
                    R.id.action_delete -> presenter.deletePhoto()
                    else -> return@setOnMenuItemClickListener false
                }
                return@setOnMenuItemClickListener true
            }
        }
    }

    override fun getLayoutResId() = R.layout.activity_customer_registration

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent?.inject(this)
        presenter.bind(this)
        toolbar.setTitle(R.string.registration_title)
        toolbar.setBackArrowAndAction { finish() }
        editTextPhone.addPhoneTextWatcher()
        initView()
        checkValidation()
        imageViewEditAvatar.setOnClickListener {
            presenter.showPopupMenu()
        }
        editTextCity.setOnClickListener {
            presenter.showGeoObjectsDialog()
        }
        buttonRegister.setOnClickListener {
            presenter.registrationAttempt(
                    editTextName.text.toString(),
                    editTextOrganization.text.toString(),
                    editTextPhone.text.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Picker.PICK_IMAGE_DEVICE -> internalStoragePhotoPicker.submit(data)
                Picker.PICK_IMAGE_CAMERA -> cameraImagePicker.submit(data)
            }
        }
    }

    override fun onDestroy() {
        presenter.destroy()
        if (!disposableSubscriber.isDisposed) {
            disposableSubscriber.dispose()
        }
        super.onDestroy()
    }

    private fun initView() {
        nameFlowable = editTextName.textChanges().skip(1).map { it.toString() }.toFlowable(BackpressureStrategy.LATEST)
        phoneFlowable = editTextPhone.textChanges().skip(1).map { it.toString() }.toFlowable(BackpressureStrategy.LATEST)
        cityFlowable = editTextCity.textChanges().skip(1).map { it.toString() }.toFlowable(BackpressureStrategy.LATEST)
    }

    private fun checkValidation() {

        disposableSubscriber = object : DisposableSubscriber<Boolean>() {
            override fun onNext(enabled: Boolean) {
                buttonRegister.isEnabled = enabled
            }

            override fun onError(t: Throwable) {
                Timber.d(t)
            }

            override fun onComplete() {
            }
        }

        Flowable
                .combineLatest(nameFlowable, phoneFlowable, cityFlowable, Function3<String, String, String, Boolean> { name, newPhone, city ->
                    val nameValid = !TextUtils.isEmpty(name)
                    val phone = newPhone.filter { it.isDigit() }
                    val phoneValid = phone.length > 9
                    val cityValid = city.isNotEmpty()
                    isValidForm = nameValid && phoneValid && cityValid
                    return@Function3 isValidForm
                })
                .subscribe(disposableSubscriber)

    }

    override fun showProgressDialog() = showProgressAlert()

    override fun hideProgressDialog() = hideProgressAlert()

    override fun showError(message: String) = showSnackbar(editTextName, message)

    override fun showError(@StringRes messageRes: Int) = showSnackbar(editTextName, getString(messageRes))

    override fun openCheckCodeActivity(code: String, phone: String) {
        startActivity<CheckCodeActivity>(EXTRA_PHONE_CODE to code, EXTRA_PHONE_NUMBER to phone)
    }

    override fun showGeoObjectsDialog() {
        val dialog = SelectGeoObjectDialog.createDialog(CITIES)
        dialog.onObjectClick { presenter.geoObjectSelected(it) }
        dialog.show(supportFragmentManager, "TAG")
    }

    override fun geoObjectSelected(geoObject: GeoObject) {
        editTextCity.setText(geoObject.name)
    }

    override fun showPopupMenu(showDelete: Boolean) {
        with(popupMenu) {
            menu.findItem(R.id.action_delete).isVisible = showDelete
            show()
        }
    }

    override fun takePhoto() {
        rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe { granted ->
                    if (granted) {
                        cameraImagePicker.pickImage()
                    }
                }
    }

    override fun chooseFromGallery() {
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe { granted ->
                    if (granted) {
                        internalStoragePhotoPicker.pickImage()
                    }
                }
    }

    override fun deletePhoto() {
        imageViewAvatar.setImageResource(R.drawable.ic_person)
    }

    override fun cropPhoto(uri: Uri) {
        waitForActivityResult(ImageCropActivity.createStartIntent(this, uri)) { intent ->
            presenter.photoCropped(intent.getParcelableExtra(ImageCropActivity.IMAGE_URI))
        }
        hideProgressAlert()
    }

    override fun showSelectedAvatar(uri: Uri) {
        imageViewAvatar.setImageURI(uri)
    }
}
