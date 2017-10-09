package ru.mydispatcher.ui.common

import android.view.View
import android.widget.Button
import android.widget.TextView
import ru.mydispatcher.util.extensions.hide
import ru.mydispatcher.util.extensions.show

interface LoadingManager {
    val viewLoading: View
    val viewError: View
    val viewData: View
    val textViewErrorMessage: TextView
    val buttonRetry: Button

    fun showLoading() {
        viewData.hide()
        viewError.hide()
        viewLoading.show()
    }

    fun showError() {
        viewLoading.hide()
        viewData.hide()
        viewError.show()
    }

    fun showData() {
        viewData.show()
        viewError.hide()
        viewLoading.hide()
    }

    fun showErrorMessage(message: String?) {
        message?.let { textViewErrorMessage.text = it }
    }

    fun showErrorMessage(messageId: Int?) {
        messageId?.let {
            textViewErrorMessage.apply {
                val message = this.resources.getString(messageId)
                text = message
            }
        }
    }
}