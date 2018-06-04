package com.kitobim.data.local.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "library",
        foreignKeys = [(ForeignKey(entity = BookEntity::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("book_id")))])

class LibraryEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val book_id: Int,
        val downloaded: Boolean
)