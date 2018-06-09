package com.kitobim.data.local.database.dao

import android.arch.persistence.room.*
import com.kitobim.data.local.database.entity.GenreEntity


@Dao
interface GenreDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(genre: GenreEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(genre: GenreEntity): Int

    @Query("DELETE FROM genres")
    fun deleteAll()
}