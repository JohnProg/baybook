package com.kitobim.data.local.database.coverter

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kitobim.data.local.database.entity.AuthorEntity

class DataConverter {

    @TypeConverter
    fun fromAuthorList(value: List<AuthorEntity>): String {
        val gson = Gson()
        val type = object : TypeToken<List<AuthorEntity>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toAuthorList(value: String): List<AuthorEntity> {
        val gson = Gson()
        val type = object : TypeToken<List<AuthorEntity>>() {}.type
        return gson.fromJson(value, type)
    }
}