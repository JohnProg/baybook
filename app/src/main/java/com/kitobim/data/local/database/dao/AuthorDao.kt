package com.kitobim.data.local.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.kitobim.data.local.database.entity.AuthorEntity


@Dao
interface AuthorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(author: AuthorEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(authors: List<AuthorEntity>)

    @Update
    fun update(author: AuthorEntity): Int

    @Query("DELETE FROM authors")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM authors")
    fun getNumberOfRows(): Int

    @Query("SELECT COUNT(page) FROM authors WHERE page = :page")
    fun getNumberOfThisPage(page: Int): Int

    @Query("SELECT * FROM authors ORDER BY name ASC")
    fun loadAllAuthors(): LiveData<List<AuthorEntity>>
}