package com.kitobim.data.local.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.kitobim.data.local.database.entity.GenreEntity
import com.kitobim.data.local.database.entity.GenreListEntity
import io.reactivex.Maybe


@Dao
interface GenreListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(author: GenreListEntity)

    @Query("DELETE FROM genre_list")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM genre_list WHERE genre_list.page=:page")
    fun getRowCountOfPage(page: Int): Maybe<Int>

    @Query("SELECT id,name,image,books_count FROM genres INNER JOIN genre_list ON genre_list.genre_id=genres.id")
    fun loadAllGenres(): LiveData<List<GenreEntity>>
}
