package com.kitobim.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.kitobim.repository.PaidBooksRepository


class PaidBookViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PaidBooksRepository.getInstance(application)

    fun getAllBooks() = repository.loadAllBooks()

    fun insertAll() {
        repository.insertAll()
    }
}