package dev.fummicc1.lit.bookshelf.datas

import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let {
        Date(it)
    }

    @TypeConverter
    fun dateToTimeStamp(date: Date?): Long? = date?.time?.toLong()
}