package com.example.yaroslavyadrov.cargofinder.ui.customer.customerroute

import android.os.Bundle
import com.example.yaroslavyadrov.cargofinder.R
import com.example.yaroslavyadrov.cargofinder.ui.base.BaseActivity
import javax.inject.Inject


class CustomerRouteActivity : BaseActivity(), CustomerRouteView {
    @Inject lateinit var presenter: CustomerRoutePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        activityComponent?.inject(this)
        presenter.bind(this)
    }

    override fun getLayoutResId() = R.layout.layout_customer_route

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }
}