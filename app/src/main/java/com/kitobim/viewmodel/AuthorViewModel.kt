package com.kitobim.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.kitobim.repository.AuthorListRepository


class AuthorViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AuthorListRepository.getInstance(application)

    fun getAllBooks() = repository.loadAllAuthors()

    fun insertAll(page: Int) {
        repository.insertAll(page)
    }
}