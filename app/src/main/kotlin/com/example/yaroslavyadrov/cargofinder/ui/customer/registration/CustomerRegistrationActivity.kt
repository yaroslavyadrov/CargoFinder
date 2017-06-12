package com.example.yaroslavyadrov.cargofinder.ui.customer.registration

import com.example.yaroslavyadrov.cargofinder.R
import com.example.yaroslavyadrov.cargofinder.ui.base.BaseActivity
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
