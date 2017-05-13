package com.example.yaroslavyadrov.cargofinder.injection.module

import com.example.yaroslavyadrov.cargofinder.data.DataManager
import dagger.Binds
import dagger.Module


@Module
abstract class Bindings {

    @Binds
    internal abstract fun bindDataManger(manager: DataManager): DataManager

}