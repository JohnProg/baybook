package com.kitobim.data.local.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "books")
class BookEntity (
        @PrimaryKey
        val id: Int,
        val title: String,
        val thumbnail: String?,
        val authors: String?,
        val price: Int
)