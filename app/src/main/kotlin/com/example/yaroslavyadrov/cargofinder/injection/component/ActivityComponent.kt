package com.example.yaroslavyadrov.cargofinder.injection.component

import com.example.yaroslavyadrov.cargofinder.injection.scope.PerActivity
import com.example.yaroslavyadrov.cargofinder.ui.main.MainActivity
import dagger.Subcomponent


@PerActivity
@Subcomponent
interface ActivityComponent {

    fun inject(activity: MainActivity)

}