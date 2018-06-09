package com.kitobim.data.local.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "books")
class BookEntity (
        @PrimaryKey
        val id: Int,
        val title: String,
        val thumbnail: String?,
        val authors: List<AuthorEntity>,
        val price: Int,
        val rating: Float? = null,
        val purchased: Boolean = false,
        val inwishlist: Boolean = false
)