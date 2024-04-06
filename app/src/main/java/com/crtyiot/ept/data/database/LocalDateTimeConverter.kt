package com.crtyiot.ept.data.database



import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeConverter {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

        @RequiresApi(Build.VERSION_CODES.O)
        @TypeConverter
        @JvmStatic
        fun toLocalDateTime(value: String?): LocalDateTime? {
            return value?.let {
                LocalDateTime.parse(it, formatter)
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        @TypeConverter
        @JvmStatic
        fun fromLocalDateTime(date: LocalDateTime?): String? {
            return date?.format(formatter)
        }
    }
}