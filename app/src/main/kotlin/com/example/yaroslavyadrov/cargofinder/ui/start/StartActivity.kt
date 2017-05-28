package com.example.yaroslavyadrov.cargofinder.ui.start

import android.os.Bundle
import com.example.yaroslavyadrov.cargofinder.R
import com.example.yaroslavyadrov.cargofinder.ui.base.BaseActivity
import com.example.yaroslavyadrov.cargofinder.ui.customer.customerroute.CustomerRouteActivity
import com.example.yaroslavyadrov.cargofinder.util.UserType
import com.example.yaroslavyadrov.cargofinder.util.extensions.launchActivity
import com.example.yaroslavyadrov.cargofinder.util.extensions.showSnackbar
import kotlinx.android.synthetic.main.layout_activity_main.*
import javax.inject.Inject


class StartActivity : BaseActivity(), StartView {

    @Inject lateinit var presenter: StartPresenter

    override fun getLayoutResId() = R.layout.layout_activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent?.inject(this)
        presenter.bind(this)
        buttonCustomer.setOnClickListener { presenter.getGuestToken(UserType.CUSTOMER, "123") }
        buttonDriver.setOnClickListener { presenter.getGuestToken(UserType.DRIVER, "123") }
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }

    override fun openCustomerAuthActivity() {
        launchActivity<CustomerRouteActivity>()
    }

    override fun openDriverAuthActivity() {
        showSnackbar(buttonCustomer, "driver")
    }

    override fun showProgressDialog() {
        showProgressAlert()
    }

    override fun hideProgressDialog() {
        hideProgressAlert()
    }

    override fun showError(message: String) {
        showSnackbar(buttonCustomer, message)
    }
}

