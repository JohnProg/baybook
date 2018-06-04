package com.kitobim.data.local.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "new_books",
        foreignKeys = [(ForeignKey(entity = BookEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("book_id")))])

data class NewBooksEntity (
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val book_id: Int,
        val page: Int = 0
)