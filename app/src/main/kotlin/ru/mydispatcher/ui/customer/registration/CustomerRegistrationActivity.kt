package ru.mydispatcher.ui.customer.registration


import android.text.TextUtils
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import io.reactivex.subscribers.DisposableSubscriber
import kotlinx.android.synthetic.main.activity_customer_registration.*
import kotlinx.android.synthetic.main.view_appbar_with_toolbar.*
import ru.mydispatcher.R
import ru.mydispatcher.ui.base.BaseActivity
import ru.mydispatcher.ui.selectgeoobject.SelectGeoObjectDialog
import ru.mydispatcher.ui.selectgeoobject.SelectGeoObjectDialog.Companion.CITIES
import ru.mydispatcher.util.extensions.addPhoneTextWatcher
import ru.mydispatcher.util.extensions.setBackArrowAndFinishActionOnToolbar
import timber.log.Timber
import javax.inject.Inject


class CustomerRegistrationActivity : BaseActivity(), CustomerRegistrationMvpView {

    @Inject lateinit var presenter: CustomerRegistrationPresenter

    lateinit var nameFlowable: Flowable<String>
    lateinit var phoneFlowable: Flowable<String>
    private var isValidForm: Boolean = false
    lateinit private var disposableSubscriber: DisposableSubscriber<Boolean>

    override fun getLayoutResId() = R.layout.activity_customer_registration

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent?.inject(this)
        presenter.bind(this)
        toolbar.setTitle(R.string.registration_title)
        setBackArrowAndFinishActionOnToolbar()
        editTextPhone.addPhoneTextWatcher()
        initView()
        checkValidation()
        editTextCity.setOnClickListener {
            val dialog = SelectGeoObjectDialog.createDialog(CITIES)
            dialog.show(supportFragmentManager, "TAG")
        }
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
        if (!disposableSubscriber.isDisposed) {
            disposableSubscriber.dispose()
        }
    }

    private fun initView() {
        nameFlowable = editTextName.textChanges().skip(1).map { it.toString() }.toFlowable(BackpressureStrategy.LATEST)
        phoneFlowable = editTextPhone.textChanges().skip(1).map { it.toString() }.toFlowable(BackpressureStrategy.LATEST)
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

        Flowable.combineLatest(nameFlowable, phoneFlowable, BiFunction<String, String, Boolean> { name, newPhone ->
            val nameValid = !TextUtils.isEmpty(name)
            val phone = newPhone.filter { it.isDigit() }
            val phoneValid = phone.length > 9
            isValidForm = nameValid && phoneValid
            return@BiFunction isValidForm
        }).subscribe({
            buttonRegister.isEnabled = it
        })

    }
}
