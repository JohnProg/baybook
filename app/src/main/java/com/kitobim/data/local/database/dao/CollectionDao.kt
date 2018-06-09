package com.kitobim.data.local.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.kitobim.data.local.database.entity.CollectionEntity
import io.reactivex.Maybe

@Dao
interface CollectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(collection: CollectionEntity)

    @Query("DELETE FROM collections")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM collections")
    fun getRowCount(): Maybe<Int>

    @Query("SELECT * FROM collections")
    fun loadAllCollections(): LiveData<List<CollectionEntity>>
}