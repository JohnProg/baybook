package com.kitobim.data.local.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.kitobim.data.local.database.entity.BookEntity
import com.kitobim.data.local.database.entity.NewBooksEntity


@Dao
interface NewBooksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(book: NewBooksEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(books: List<NewBooksEntity>)

    @Update
    fun update(book: NewBooksEntity): Int

    @Query("DELETE FROM new_books")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM new_books")
    fun getNumberOfRows(): Int

    @Query("SELECT COUNT(page) FROM new_books WHERE page = :page")
    fun getNumberOfThisPage(page: Int): Int

    @Query("SELECT * FROM books INNER JOIN new_books ON new_books.book_id = books.id")
    fun loadAllBooks(): LiveData<List<BookEntity>>
}