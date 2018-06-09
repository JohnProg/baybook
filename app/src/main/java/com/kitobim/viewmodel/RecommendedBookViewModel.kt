package com.kitobim.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.kitobim.repository.RecommendedBooksRepository


class RecommendedBookViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = RecommendedBooksRepository.getInstance(application)

    fun getAllBooks() = repository.loadAllBooks()

    fun insertAll() {
        repository.insertAll()
    }
}