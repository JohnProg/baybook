package com.kitobim.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.kitobim.repository.NewBooksRepository


class NewBookViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = NewBooksRepository.getInstance(application)
    private var mObservableBook = repository.loadAllBooks()

    fun getAllBooks() = mObservableBook
    fun insertAll(page: Int) {
        repository.insertAll(page)
    }
}