package com.example.yaroslavyadrov.cargofinder.util.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.view.View
import android.widget.EditText
import com.example.yaroslavyadrov.cargofinder.CargoFinderApplication
import com.example.yaroslavyadrov.cargofinder.injection.component.AppComponent

fun Context.getAppComponent(): AppComponent = (applicationContext as CargoFinderApplication).appComponent

fun showSnackbar(view: View, text: String) {
    Snackbar.make(view, text, Snackbar.LENGTH_LONG).show()
}

inline fun <reified T : Any> Activity.launchActivity(
        options: Bundle? = null,
        noinline init: Intent.() -> Unit = {}) {

    val intent = newIntent<T>(this)
    intent.init()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        startActivity(intent, options)
    } else {
        startActivity(intent)
    }
}

inline fun <reified T : Any> Activity.launchActivity(
        requestCode: Int = -1,
        options: Bundle? = null,
        noinline init: Intent.() -> Unit = {}) {

    val intent = newIntent<T>(this)
    intent.init()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        startActivityForResult(intent, requestCode, options)
    } else {
        startActivityForResult(intent, requestCode)
    }
}


inline fun <reified T : Any> newIntent(context: Context): Intent =
        Intent(context, T::class.java)

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