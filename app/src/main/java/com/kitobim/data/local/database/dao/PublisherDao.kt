package com.kitobim.data.local.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.kitobim.data.local.database.entity.PublisherEntity
import io.reactivex.Maybe

@Dao
interface PublisherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(publisher: PublisherEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(publisher: PublisherEntity): Int

    @Query("DELETE FROM publishers")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM publishers")
    fun getRowCount(): Maybe<Int>

    @Query("SELECT * FROM publishers")
    fun loadAllPublishers(): LiveData<List<PublisherEntity>>
}