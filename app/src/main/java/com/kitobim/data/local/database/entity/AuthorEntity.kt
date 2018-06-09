package com.kitobim.data.local.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "authors")
class AuthorEntity (
    @PrimaryKey
    val id: Int = 0,
    val name: String,
    val thumbnail: String? = null,
    val books_count: Int = 0
)