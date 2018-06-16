package com.kitobim.data.local.database.entity

import android.arch.persistence.room.*

@Entity(tableName = "author_list",
        indices = [Index(value = ["author_id"])],
        foreignKeys = [(ForeignKey(entity = AuthorEntity::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("author_id"),
                onDelete = ForeignKey.CASCADE))])

class AuthorListEntity (
        @PrimaryKey
        @ColumnInfo(name = "author_id")
        val author_id: Int,
        val page: Int
)