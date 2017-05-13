package com.example.yaroslavyadrov.cargofinder.injection.module

import com.example.yaroslavyadrov.cargofinder.data.DataManager
import com.example.yaroslavyadrov.cargofinder.data.DataManagerImpl
import dagger.Binds
import dagger.Module


@Module
abstract class Bindings {

    @Binds
    internal abstract fun bindDataManger(manager: DataManagerImpl): DataManager

}