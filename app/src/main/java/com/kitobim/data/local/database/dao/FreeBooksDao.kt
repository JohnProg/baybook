package com.kitobim.data.local.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.kitobim.data.local.database.entity.BookEntity
import com.kitobim.data.local.database.entity.FreeBooksEntity
import io.reactivex.Maybe


@Dao
interface FreeBooksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(book: FreeBooksEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(books: List<FreeBooksEntity>)

    @Query("DELETE FROM free_books")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM free_books")
    fun getRowCount(): Maybe<Int>

    @Query("SELECT id,title,thumbnail,authors,price,rating,inwishlist,purchased FROM books INNER JOIN free_books ON free_books.book_id=books.id")
    fun loadAllBooks(): LiveData<List<BookEntity>>
}