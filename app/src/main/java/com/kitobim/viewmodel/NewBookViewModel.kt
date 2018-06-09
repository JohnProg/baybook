package com.kitobim.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.kitobim.repository.NewBooksRepository


class NewBookViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = NewBooksRepository.getInstance(application)

    fun getAllBooks() = repository.loadAllBooks()

    fun insertAll(page: Int) {
        repository.insertAll(page)
    }
}