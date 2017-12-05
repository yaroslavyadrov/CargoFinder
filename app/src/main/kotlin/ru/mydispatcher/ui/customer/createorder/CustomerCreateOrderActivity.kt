package ru.mydispatcher.ui.customer.createorder

import android.os.Bundle
import ru.mydispatcher.R
import ru.mydispatcher.ui.base.BaseActivity
import javax.inject.Inject

class CustomerCreateOrderActivity : BaseActivity(), CustomerCreateOrderMvpView {

    @Inject lateinit var presenter: CustomerCreateOrderPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_create_order)
        activityComponent?.inject(this)
        presenter.bind(this)
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }
}