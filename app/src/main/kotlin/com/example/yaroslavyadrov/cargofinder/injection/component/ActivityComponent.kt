package com.example.yaroslavyadrov.cargofinder.injection.component

import com.example.yaroslavyadrov.cargofinder.injection.scope.PerActivity
import com.example.yaroslavyadrov.cargofinder.ui.checkcode.CheckCodeActivity
import com.example.yaroslavyadrov.cargofinder.ui.customer.registration.CustomerRegistrationActivity
import com.example.yaroslavyadrov.cargofinder.ui.start.StartActivity
import dagger.Subcomponent


@PerActivity
@Subcomponent
interface ActivityComponent {

    fun inject(activity: StartActivity)
    fun inject(activity: CustomerRegistrationActivity)
    fun inject(activity: CheckCodeActivity)

}