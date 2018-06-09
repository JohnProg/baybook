package com.kitobim.data.local.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.kitobim.data.local.database.entity.BookEntity
import com.kitobim.data.local.database.entity.RecommendedBooksEntity
import io.reactivex.Maybe


@Dao
interface RecommendedBooksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(book: RecommendedBooksEntity)

    @Query("DELETE FROM recommended_books")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM recommended_books")
    fun getRowCount(): Maybe<Int>

    @Query("SELECT id,title,thumbnail,authors,price,rating,inwishlist,purchased FROM books INNER JOIN recommended_books ON recommended_books.book_id = books.id")
    fun loadAllBooks(): LiveData<List<BookEntity>>
}