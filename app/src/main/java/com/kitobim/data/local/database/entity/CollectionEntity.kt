package com.kitobim.data.local.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "collections")
class CollectionEntity (
    @PrimaryKey
    val id: Int,
    val title: String,
    val books: Int
)