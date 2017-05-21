package com.example.yaroslavyadrov.cargofinder.ui.main

import com.example.yaroslavyadrov.cargofinder.data.DataManager
import com.example.yaroslavyadrov.cargofinder.data.model.CargoFinderException
import com.example.yaroslavyadrov.cargofinder.injection.scope.PerActivity
import com.example.yaroslavyadrov.cargofinder.ui.base.BasePresenter
import javax.inject.Inject

@PerActivity
class MainPresenter @Inject constructor(val dataManager: DataManager) : BasePresenter<MainView>() {

    fun fetchJokes() {
        disposables.add(
                dataManager.getGuestToken("some", "some")
                        .subscribe(
                                { val i = 1 },
                                { e ->
                                    run {
                                        when (e) {
                                            is CargoFinderException -> {
                                                e.message
                                            }
                                            else -> {
                                            }
                                        }
                                    }
                                })
        )
    }

}