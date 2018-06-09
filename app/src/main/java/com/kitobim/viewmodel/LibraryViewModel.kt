package com.kitobim.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.kitobim.data.local.database.entity.LibraryEntity
import com.kitobim.repository.LibraryRepository


class LibraryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = LibraryRepository.getInstance(application)

    fun getAllBooks() = repository.loadAllBooks()

    fun insertBook(book: LibraryEntity) {
        repository.insert(book)
    }


}