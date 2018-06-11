package com.kitobim.data.local.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.kitobim.data.local.database.entity.BookEntity
import com.kitobim.data.local.database.entity.WishlistEntity
import io.reactivex.Maybe


@Dao
interface WishlistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(book: WishlistEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(books: List<WishlistEntity>)

    @Query("DELETE FROM wishlist")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM wishlist")
    fun getRowCount(): Maybe<Int>

    @Query("SELECT id,title,thumbnail,authors,price,rating,inwishlist,purchased FROM books INNER JOIN wishlist ON wishlist.book_id = books.id")
    fun loadAllBooks(): LiveData<List<BookEntity>>
}