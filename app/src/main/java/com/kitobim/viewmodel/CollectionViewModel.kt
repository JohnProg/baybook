package com.kitobim.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.kitobim.repository.CollectionRepository


class CollectionViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CollectionRepository.getInstance(application)

    fun getAllCollections() = repository.loadAllCollections()

    fun insertAll() {
        repository.insertAll()
    }
}