package com.kitobim.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.kitobim.repository.RatedBooksRepository


class RatedBookViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = RatedBooksRepository.getInstance(application)

    fun getAllBooks() = repository.loadAllBooks()
    fun insertAll() {
        repository.insertAll()
    }
}