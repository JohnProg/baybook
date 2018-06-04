package com.kitobim.data.local.database

import android.arch.persistence.room.TypeConverter
import java.sql.Date


object DateConverter {
    @TypeConverter
    fun toDate(timestamp: Long) = Date(timestamp)

    @TypeConverter
    fun toTimestamp(date: Date) = date.time
}