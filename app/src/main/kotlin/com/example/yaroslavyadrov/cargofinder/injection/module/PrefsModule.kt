package com.example.yaroslavyadrov.cargofinder.injection.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PrefsModule constructor(private val app: Application) {

    companion object {
        const val PREF_FILE_NAME = "cargofinder_preferences"
    }

    @Provides
    @Singleton
    internal fun providePrefs(): SharedPreferences {
        return app.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
    }
}