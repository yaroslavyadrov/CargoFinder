package com.example.yaroslavyadrov.cargofinder.ui.checkcode

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.example.yaroslavyadrov.cargofinder.R
import com.example.yaroslavyadrov.cargofinder.ui.base.BaseActivity
import com.example.yaroslavyadrov.cargofinder.util.emptyAnimator
import com.example.yaroslavyadrov.cargofinder.util.extensions.setBackArrowAndFinishActionOnToolbar
import kotlinx.android.synthetic.main.activity_check_code.*
import kotlinx.android.synthetic.main.view_appbar_with_toolbar.*
import timber.log.Timber
import javax.inject.Inject

fun Context.checkCodeIntent(code: String, phone: String): Intent {
    return Intent(this, CheckCodeActivity::class.java).apply {
        putExtra(EXTRA_PHONE_CODE, code)
        putExtra(EXTRA_PHONE_NUMBER, phone)
    }
}
private const val EXTRA_PHONE_CODE = "EXTRAS.extra_phone_code"
private const val EXTRA_PHONE_NUMBER = "EXTRAS.extra_phone_number"

class CheckCodeActivity : BaseActivity(), CheckCodeMvpView {
    override fun getLayoutResId() = R.layout.activity_check_code

    @Inject lateinit var presenter: CheckCodePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent?.inject(this)
        presenter.bind(this)
        toolbar.setTitle(R.string.check_code_title)
        setBackArrowAndFinishActionOnToolbar()
        val code = intent.getStringExtra(EXTRA_PHONE_CODE) ?: throw RuntimeException("Value code needed")
        val phone = intent.getStringExtra(EXTRA_PHONE_NUMBER) ?: throw RuntimeException("Value phone needed")
        editTextSymbol5.removeTextChangedListener(editTextSymbol5)
        editTextSymbol5.addTextChangedListener(object : TextWatcher by emptyAnimator {
            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty()) {
                    Timber.d("code!")
                }
            }
        })
        editTextSymbol1.setFocusableInTouchMode(true)
        editTextSymbol1.requestFocus()
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }


}
