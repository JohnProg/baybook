package com.kitobim.data.local.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.kitobim.data.local.database.entity.AuthorEntity


@Dao
interface AuthorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(author: AuthorEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(author: AuthorEntity): Int

    @Query("DELETE FROM authors")
    fun deleteAll()

    @Query("SELECT * FROM authors WHERE id=:id")
    fun getAuthorById(id: Int): LiveData<AuthorEntity>
}