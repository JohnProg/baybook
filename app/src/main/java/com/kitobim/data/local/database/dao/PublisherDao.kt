package com.kitobim.data.local.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.kitobim.data.local.database.entity.PublisherEntity

@Dao
interface PublisherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(publisher: PublisherEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(publishers: List<PublisherEntity>)

    @Update
    fun update(publisher: PublisherEntity): Int

    @Query("DELETE FROM publishers")
    fun deleteAll()

    @Query("SELECT * FROM publishers")
    fun loadAllPublishers(): LiveData<List<PublisherEntity>>
}