package com.kitobim.data.local.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.kitobim.data.local.database.entity.BookEntity
import com.kitobim.data.local.database.entity.LibraryEntity

@Dao
interface LibraryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(book: LibraryEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(books: List<LibraryEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(book: LibraryEntity): Int

    @Delete
    fun delete(book: LibraryEntity): Int

    @Query("DELETE FROM library")
    fun deleteAll()

    @Query("SELECT * FROM books INNER JOIN library ON books.id = library.book_id")
    fun loadAllBooks(): LiveData<List<BookEntity>>

    @Query("SELECT * FROM books INNER JOIN library ON books.id = library.book_id ORDER BY title ASC")
    fun loadBooksByNameAsc(): LiveData<List<BookEntity>>

    @Query("SELECT * FROM books INNER JOIN library ON books.id = library.book_id ORDER BY title DESC")
    fun loadBooksByNameDesc(): LiveData<List<BookEntity>>

//    @Query("SELECT * FROM library ORDER BY last_read DESC")
//    fun loadBooksByLastRead(): LiveData<List<LibraryEntity>>
}