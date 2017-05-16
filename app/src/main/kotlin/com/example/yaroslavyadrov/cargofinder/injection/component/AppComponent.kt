package com.example.yaroslavyadrov.cargofinder.injection.component

import com.example.yaroslavyadrov.cargofinder.injection.module.NetworkModule
import com.example.yaroslavyadrov.cargofinder.injection.module.PrefsModule
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(NetworkModule::class, PrefsModule::class))
interface AppComponent {

    fun activityComponent(): ActivityComponent

}