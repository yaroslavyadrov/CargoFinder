package ru.mydispatcher.ui.customer.registration

import ru.mydispatcher.R
import ru.mydispatcher.ui.base.BaseActivity
import javax.inject.Inject

class CustomerRegistrationActivity : BaseActivity(), CustomerRegistrationMvpView {

    @Inject lateinit var presenter: CustomerRegistrationPresenter

    override fun getLayoutResId() = R.layout.activity_customer_registration

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent?.inject(this)
        presenter.bind(this)
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }
}
