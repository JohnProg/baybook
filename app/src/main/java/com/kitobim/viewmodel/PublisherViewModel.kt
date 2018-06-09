package com.kitobim.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.kitobim.repository.PublisherRepository


class PublisherViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PublisherRepository.getInstance(application)

    fun getAllPublishers() = repository.loadAllPublishers()

    fun insertAll() {
        repository.insertAll()
    }
}