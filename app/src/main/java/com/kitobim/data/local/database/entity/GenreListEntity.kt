package com.kitobim.data.local.database.entity

import android.arch.persistence.room.*

@Entity(tableName = "genre_list",
        indices = [(Index(value = ["genre_id"], unique = true))],
        foreignKeys = [(ForeignKey(entity = GenreEntity::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("genre_id"),
                onDelete = ForeignKey.CASCADE))])

class GenreListEntity (
        @PrimaryKey
        @ColumnInfo(name = "genre_id")
        val genre_id: Int,
        val page: Int = 0
)