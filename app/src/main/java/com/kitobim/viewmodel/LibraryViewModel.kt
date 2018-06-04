package com.kitobim.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.kitobim.data.local.database.entity.LibraryEntity
import com.kitobim.repository.LibraryRepository


class LibraryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = LibraryRepository.getInstance(application)
    private var mObservableBook = repository.loadAllBooks()


    fun getAllBooks() = mObservableBook

    fun insertBook(book: LibraryEntity) {
        repository.insert(book)
    }


}