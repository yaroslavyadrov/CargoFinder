package ru.mydispatcher.util.extensions

import android.content.Context
import android.net.Uri
import android.support.design.widget.Snackbar
import android.support.v7.widget.Toolbar
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.EditText
import okhttp3.MediaType
import okhttp3.RequestBody
import ru.mydispatcher.CargoFinderApplication
import ru.mydispatcher.R
import ru.mydispatcher.injection.component.AppComponent
import ru.mydispatcher.ui.base.BaseActivity
import java.io.File

fun Context.getAppComponent(): AppComponent = (applicationContext as CargoFinderApplication).appComponent

fun showSnackbar(view: View, text: String) {
    Snackbar.make(view, text, Snackbar.LENGTH_LONG).show()
}

fun EditText.addPhoneTextWatcher() {
    this.addTextChangedListener(object : PhoneNumberFormattingTextWatcher() {
        private var backspacingFlag = false
        private var editedFlag = false
        private var cursorComplement: Int = 0

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            cursorComplement = s.length - this@addPhoneTextWatcher.selectionStart
            backspacingFlag = count > after
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            // nothing to do here =D
        }

        override fun afterTextChanged(s: Editable) {
            val string = s.toString()
            val phone = string.replace("[^\\d]".toRegex(), "")
            if (!editedFlag) {
                if (phone.length >= 6 && !backspacingFlag) {
                    editedFlag = true
                    val ans = "(" + phone.substring(0, 3) + ") " + phone.substring(3, 6) + "-" + phone.substring(6)
                    this@addPhoneTextWatcher.setText(ans)
                    this@addPhoneTextWatcher.setSelection(this@addPhoneTextWatcher.text.length - cursorComplement)
                } else if (phone.length >= 3 && !backspacingFlag) {
                    editedFlag = true
                    val ans = "(" + phone.substring(0, 3) + ") " + phone.substring(3)
                    this@addPhoneTextWatcher.setText(ans)
                    this@addPhoneTextWatcher.setSelection(this@addPhoneTextWatcher.text.length - cursorComplement)
                }
            } else {
                editedFlag = false
            }
        }
    })
}

fun Toolbar.setBackArrowAndAction(action: () -> Unit) {
    setNavigationIcon(R.drawable.ic_arrow_back)
    setNavigationOnClickListener { action() }
}

fun String.toRequestBody(): RequestBody {
    return RequestBody.create(MediaType.parse("text/plain"), this)
}

fun File.toRequestBody(): RequestBody {
    val uri = Uri.fromFile(this)
    val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
    val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase())
    return RequestBody.create(MediaType.parse(mimeType), this)
}

fun String.onlyDigits(): String {
    return this.filter { it.isDigit() }
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}