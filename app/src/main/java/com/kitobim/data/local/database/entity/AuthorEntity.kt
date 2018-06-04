package com.kitobim.data.local.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "authors")
class AuthorEntity (
    @PrimaryKey
    val id: Int,
    val name: String,
    val thumbnail: String,
    val books: Int,
    val page:Int = 0
)