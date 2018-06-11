package com.kitobim.data.local.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.kitobim.data.local.database.entity.AuthorEntity
import com.kitobim.data.local.database.entity.AuthorListEntity
import io.reactivex.Maybe


@Dao
interface AuthorListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(author: AuthorListEntity)

    @Query("DELETE FROM author_list")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM author_list WHERE author_list.page=:page")
    fun getRowCountOfPage(page: Int): Maybe<Int>

    @Query("SELECT id,name,thumbnail,books_count FROM authors INNER JOIN author_list ON author_list.author_id=authors.id ORDER BY authors.name ASC")
    fun loadAllAuthors(): LiveData<List<AuthorEntity>>

    @Query("SELECT id,name,thumbnail,books_count FROM authors INNER JOIN author_list ON author_list.author_id=authors.id WHERE page=:page ORDER BY authors.name ASC ")
    fun loadAuthorByPage(page: Int): LiveData<List<AuthorEntity>>
}
