package com.kitobim.ui.custom

import com.kitobim.data.local.database.entity.AuthorEntity
import com.kitobim.data.local.database.entity.BookEntity
import com.kitobim.data.local.database.entity.GenreEntity

interface FrontStoreListener {
    fun setPinnedBooks(list: List<BookEntity>)
    fun setTopMonthBooks(list: List<BookEntity>)
    fun setRandomBooks(list: List<BookEntity>)
    fun setAuthors(list: List<AuthorEntity>)
    fun setGenres(list: List<GenreEntity>)
}