package com.kitobim.data.local.database.entity

import android.arch.persistence.room.*

@Entity(tableName = "library",
        indices = [Index(value = ["book_id"], unique = true)],
        foreignKeys = [(ForeignKey(entity = BookEntity::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("book_id"),
                onDelete = ForeignKey.CASCADE))])

class LibraryEntity(
        @PrimaryKey
        @ColumnInfo(name = "book_id")
        val book_id: Int,
        val downloaded: Boolean
)