package ru.mydispatcher.ui.start

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.startActivity
import ru.mydispatcher.data.DataManager
import ru.mydispatcher.ui.customer.orders.CustomerOrdersActivity
import ru.mydispatcher.ui.login.LoginActivity
import ru.mydispatcher.util.extensions.getAppComponent
import javax.inject.Inject

class StartActivity : AppCompatActivity() {

    @Inject lateinit var dataManager: DataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getAppComponent().activityComponent().inject(this)
        if (dataManager.authorized) {
            startActivity<CustomerOrdersActivity>()
        } else {
            startActivity<LoginActivity>()
        }
    }
}