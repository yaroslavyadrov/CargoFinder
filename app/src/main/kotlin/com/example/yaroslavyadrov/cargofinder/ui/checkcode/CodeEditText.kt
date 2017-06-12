package com.example.yaroslavyadrov.cargofinder.ui.checkcode

import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputConnectionWrapper


class CodeEditText : AppCompatEditText, TextWatcher {
    constructor(context: Context) : super(context) {
        addTextChangedListener(this)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        addTextChangedListener(this)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        addTextChangedListener(this)
    }

    override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection {
        return CodeInputConnection(super.onCreateInputConnection(outAttrs), true)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun afterTextChanged(s: Editable) {
        val nextFocus = this@CodeEditText.focusSearch(FOCUS_RIGHT)
        if (nextFocus != null && s.isNotEmpty()) {
            nextFocus.requestFocus()
        }
    }

    override fun onTextChanged(text: CharSequence, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
    }

    private inner class CodeInputConnection(target: InputConnection, mutable: Boolean) : InputConnectionWrapper(target, mutable) {

        override fun sendKeyEvent(event: KeyEvent): Boolean {
            if (event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_DEL) {
                if (this@CodeEditText.length() == 0) {
                    val focusLeft = this@CodeEditText.focusSearch(FOCUS_LEFT)
                    focusLeft?.requestFocus()
                }
            }
            return super.sendKeyEvent(event)
        }
    }
}