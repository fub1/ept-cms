package com.crtyiot.ept.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.crtyiot.ept.data.Dao.TaskDao
import com.crtyiot.ept.data.Dao.MaterialDao
import com.crtyiot.ept.data.Dao.ScanDataDao
import com.crtyiot.ept.data.model.Material
import com.crtyiot.ept.data.model.ScanData
import com.crtyiot.ept.data.model.Task


@Database(entities =  [Task::class, Material::class, ScanData::class], version = 1, exportSchema = false)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun materialDao(): MaterialDao
    abstract fun scanDataDao(): ScanDataDao

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "DATABASE")
                .build()
        }
    }
}
