package com.kitobim.data.local.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.kitobim.data.local.database.entity.CollectionEntity

@Dao
interface CollectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(collection: CollectionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(collections: List<CollectionEntity>)

    @Update
    fun update(collection: CollectionEntity): Int

    @Query("DELETE FROM collections")
    fun deleteAll()

    @Query("SELECT * FROM collections")
    fun loadAllCollections(): LiveData<List<CollectionEntity>>
}