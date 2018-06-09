package com.kitobim.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.kitobim.repository.GenreListRepository


class GenreViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = GenreListRepository.getInstance(application)


    fun getAllBooks() = repository.loadAllGenres()
    fun insertAll(page: Int) {
        repository.insertAll(page)
    }
}