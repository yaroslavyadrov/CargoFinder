package ru.mydispatcher.injection.component

import ru.mydispatcher.injection.scope.PerActivity
import ru.mydispatcher.ui.checkcode.CheckCodeActivity
import ru.mydispatcher.ui.customer.registration.CustomerRegistrationActivity
import ru.mydispatcher.ui.start.StartActivity
import dagger.Subcomponent
import ru.mydispatcher.ui.selectgeoobject.SelectGeoObjectDialog


@PerActivity
@Subcomponent
interface ActivityComponent {

    fun inject(fragment: SelectGeoObjectDialog)

    fun inject(activity: StartActivity)
    fun inject(activity: CustomerRegistrationActivity)
    fun inject(activity: CheckCodeActivity)

}