package ru.mydispatcher.injection.scope

import javax.inject.Scope


@Scope
@Retention(value = AnnotationRetention.RUNTIME)
annotation class PerActivity