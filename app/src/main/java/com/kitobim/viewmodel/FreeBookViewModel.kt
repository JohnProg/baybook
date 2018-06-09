package com.kitobim.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.kitobim.repository.FreeBooksRepository


class FreeBookViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FreeBooksRepository.getInstance(application)

    fun getAllBooks() = repository.loadAllBooks()

    fun insertAll() {
        repository.insertAll()
    }
}