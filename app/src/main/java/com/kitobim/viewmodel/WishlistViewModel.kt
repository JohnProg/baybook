package com.kitobim.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.kitobim.repository.WishlistRepository


class WishlistViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = WishlistRepository.getInstance(application)

    fun getAllBooks() = repository.loadAllBooks()

    fun insertAll() {
        repository.insertAll()
    }
}