package com.example.yaroslavyadrov.cargofinder.injection.component

import com.example.yaroslavyadrov.cargofinder.injection.module.Bindings
import com.example.yaroslavyadrov.cargofinder.injection.module.NetworkModule
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(Bindings::class, NetworkModule::class))
interface AppComponent {

    fun activityComponent(): ActivityComponent

}