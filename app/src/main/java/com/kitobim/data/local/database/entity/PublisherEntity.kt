package com.kitobim.data.local.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "publishers")
class PublisherEntity (
    @PrimaryKey
    val id: Int,
    val name: String,
    val logo: String?,
    val books_count: Int
)