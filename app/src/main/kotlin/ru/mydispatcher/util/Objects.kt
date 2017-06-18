package ru.mydispatcher.util

import android.text.Editable
import android.text.TextWatcher

//Пустые интерфейсы для удобной реализации

object emptyAnimator: TextWatcher {
    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}
