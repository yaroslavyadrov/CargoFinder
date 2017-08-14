package ru.mydispatcher.injection.component

import dagger.Subcomponent
import ru.mydispatcher.injection.scope.PerActivity
import ru.mydispatcher.ui.checkcode.CheckCodeActivity
import ru.mydispatcher.ui.customer.orders.CustomerOrdersActivity
import ru.mydispatcher.ui.customer.orders.orderslist.OrdersListFragment
import ru.mydispatcher.ui.customer.registration.CustomerRegistrationActivity
import ru.mydispatcher.ui.selectgeoobject.SelectGeoObjectDialog
import ru.mydispatcher.ui.start.StartActivity


@PerActivity
@Subcomponent
interface ActivityComponent {

    fun inject(fragment: SelectGeoObjectDialog)
    fun inject(fragment: OrdersListFragment)

    fun inject(activity: StartActivity)
    fun inject(activity: CustomerRegistrationActivity)
    fun inject(activity: CheckCodeActivity)
    fun inject(activity: CustomerOrdersActivity)


}