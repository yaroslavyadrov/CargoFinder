package ru.mydispatcher.ui.checkcode

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import kotlinx.android.synthetic.main.activity_check_code.*
import kotlinx.android.synthetic.main.view_appbar_with_toolbar.*
import ru.mydispatcher.R
import ru.mydispatcher.ui.base.BaseActivity
import ru.mydispatcher.util.EXTRA_PHONE_CODE
import ru.mydispatcher.util.EXTRA_PHONE_NUMBER
import ru.mydispatcher.util.emptyWatcher
import ru.mydispatcher.util.extensions.setBackArrowAndFinishActionOnToolbar
import ru.mydispatcher.util.extensions.showSnackbar
import javax.inject.Inject


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
        presenter.showPhoneNumberDetails(code, phone)
        editTextSymbol5.removeTextChangedListener(editTextSymbol5)
        editTextSymbol1.addTextChangedListener(textWatcher)
        editTextSymbol2.addTextChangedListener(textWatcher)
        editTextSymbol3.addTextChangedListener(textWatcher)
        editTextSymbol4.addTextChangedListener(textWatcher)
        editTextSymbol5.addTextChangedListener(textWatcher)
        editTextSymbol1.isFocusableInTouchMode = true
        editTextSymbol1.requestFocus()
        presenter.sendCode(code, phone)
        textViewResend.setOnClickListener { presenter.sendCode(code, phone) }
    }

    private val textWatcher : TextWatcher = object : TextWatcher by emptyWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (!s.isNullOrEmpty()) {
                val code = editTextSymbol1.text.toString() + editTextSymbol2.text.toString() +
                        editTextSymbol3.text.toString() + editTextSymbol4.text.toString() +
                        editTextSymbol5.text.toString()
                if (code.length == 5) {
                    presenter.checkCode(code)
                }
            }
        }
    }


    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }

    override fun showProgress() = showProgressAlert()

    override fun hideProgress() = hideProgressAlert()

    override fun showError(message: String) = showSnackbar(editTextSymbol1, message)

    override fun showError(messageRes: Int) = showSnackbar(editTextSymbol1, getString(messageRes))

    override fun showDetails(phoneNumber: String) {
        textViewDetails.text = String.format(getString(R.string.check_code_details), phoneNumber)
    }

    override fun showRemain(seconds: Int) {
        val mins = seconds / 60
        val secs = seconds - (mins * 60)
        val timeText = if (mins > 0) "${mins}м. ${secs}с." else "${secs}c."
        textViewResend.apply {
            text = String.format(getString(R.string.check_code_resend_time), timeText)
            setTextColor(ContextCompat.getColor(this@CheckCodeActivity, R.color.primaryText))
            textSize = 16F
            setBackgroundResource(R.color.transparent)
            isClickable = false
        }
    }

    override fun showResend() {
        val outValue = TypedValue()
        theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        textViewResend.apply {
            text = getString(R.string.check_code_resend)
            setTextColor(ContextCompat.getColor(this@CheckCodeActivity, R.color.colorAccent))
            textSize = 18F
            setBackgroundResource(outValue.resourceId)
            isClickable = true
        }
    }
}
