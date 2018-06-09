package com.kitobim.data.local.database.entity

import android.arch.persistence.room.*

@Entity(tableName = "recommended_books",
        indices = [Index(value = ["book_id"], unique = true)],
        foreignKeys = [ForeignKey(entity = BookEntity::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("book_id"),
                onDelete = ForeignKey.CASCADE)])

class RecommendedBooksEntity (
        @PrimaryKey
        @ColumnInfo(name = "book_id")
        val book_id: Int
)