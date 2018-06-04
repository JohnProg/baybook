package com.kitobim.data.local.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "genres")
class GenreEntity (
    @PrimaryKey
    val id: Int,
    val title: String,
    val thumbnail: String,
    val books: Int,
    val page: Int = 0
)