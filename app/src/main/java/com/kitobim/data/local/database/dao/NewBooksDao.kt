package com.kitobim.data.local.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.kitobim.data.local.database.entity.BookEntity
import com.kitobim.data.local.database.entity.NewBooksEntity
import io.reactivex.Maybe


@Dao
interface NewBooksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(book: NewBooksEntity)

    @Query("DELETE FROM new_books")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM new_books WHERE new_books.page=:page")
    fun getRowCountOfPage(page: Int): Maybe<Int>

    @Query("SELECT id,title,thumbnail,authors,price,rating,inwishlist,purchased FROM books INNER JOIN new_books ON new_books.book_id = books.id")
    fun loadAllBooks(): LiveData<List<BookEntity>>
}