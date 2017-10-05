package ru.mydispatcher.ui.checkcode

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.widget.TextView
import org.jetbrains.anko.startActivity
import ru.mydispatcher.R
import ru.mydispatcher.ui.base.BaseActivity
import ru.mydispatcher.ui.customer.orders.CustomerOrdersActivity
import ru.mydispatcher.util.EXTRA_PHONE_CODE
import ru.mydispatcher.util.EXTRA_PHONE_NUMBER
import ru.mydispatcher.util.emptyWatcher
import ru.mydispatcher.util.extensions.bindView
import ru.mydispatcher.util.extensions.setBackArrowAndAction
import ru.mydispatcher.util.extensions.showSnackbar
import javax.inject.Inject


class CheckCodeActivity :
        BaseActivity(),
        CheckCodeMvpView {

    private val toolbar by bindView<Toolbar>(R.id.toolbar)
    private val editTextSymbol1 by bindView<CodeEditText>(R.id.editTextSymbol1)
    private val editTextSymbol2 by bindView<CodeEditText>(R.id.editTextSymbol2)
    private val editTextSymbol3 by bindView<CodeEditText>(R.id.editTextSymbol3)
    private val editTextSymbol4 by bindView<CodeEditText>(R.id.editTextSymbol4)
    private val editTextSymbol5 by bindView<CodeEditText>(R.id.editTextSymbol5)
    private val textViewResend by bindView<TextView>(R.id.textViewResend)
    private val textViewDetails by bindView<TextView>(R.id.textViewDetails)

    @Inject lateinit var presenter: CheckCodePresenter

    override fun getLayoutResId() = R.layout.activity_check_code
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent?.inject(this)
        presenter.bind(this)
        toolbar.setTitle(R.string.check_code_title)
        toolbar.setBackArrowAndAction { finish() }
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
        textViewResend.setOnClickListener { presenter.sendCode(code, phone) }
        presenter.onScreenOpen()
    }

    private val textWatcher: TextWatcher = object : TextWatcher by emptyWatcher {
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

    override fun openCustomerStartActivity() {
        startActivity<CustomerOrdersActivity>()
    }
}
