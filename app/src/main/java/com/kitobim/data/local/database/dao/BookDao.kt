package com.kitobim.data.local.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.kitobim.data.local.database.entity.BookEntity

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(book: BookEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(books: List<BookEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(book: BookEntity): Int

    @Query("DELETE FROM books")
    fun deleteAll()

    @Query("SELECT * FROM books WHERE id=:id")
    fun getBookById(id: Int): LiveData<BookEntity>
}