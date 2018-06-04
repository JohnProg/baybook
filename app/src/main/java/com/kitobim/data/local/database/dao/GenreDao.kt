package com.kitobim.data.local.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.kitobim.data.local.database.entity.GenreEntity


@Dao
interface GenreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(genre: GenreEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(genres: List<GenreEntity>)

    @Update
    fun update(genre: GenreEntity): Int

    @Query("DELETE FROM genres")
    fun deleteAll()


    @Query("SELECT COUNT(*) FROM genres")
    fun getNumberOfRows(): Int

    @Query("SELECT COUNT(page) FROM genres WHERE page = :page")
    fun getNumberOfThisPage(page: Int): Int

    @Query("SELECT * FROM genres")
    fun loadAllGenres(): LiveData<List<GenreEntity>>
}