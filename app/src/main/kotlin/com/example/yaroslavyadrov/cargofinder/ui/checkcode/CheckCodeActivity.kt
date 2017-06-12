package com.example.yaroslavyadrov.cargofinder.ui.checkcode

import android.os.Bundle
import com.example.yaroslavyadrov.cargofinder.R
import com.example.yaroslavyadrov.cargofinder.ui.base.BaseActivity
import javax.inject.Inject

class CheckCodeActivity : BaseActivity(), CheckCodeMvpView {
    override fun getLayoutResId() = R.layout.activity_check_code

    @Inject lateinit var presenter: CheckCodePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent?.inject(this)
        presenter.bind(this)
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }
}
