package ru.mydispatcher.injection.component

import ru.mydispatcher.injection.module.NetworkModule
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(NetworkModule::class))
interface AppComponent {

    fun activityComponent(): ActivityComponent

}