package com.crtyiot.ept.di

import android.content.Context

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

import com.crtyiot.ept.data.database.AppDatabase
import com.crtyiot.ept.data.Dao.MaterialDao
import com.crtyiot.ept.data.Dao.ScanDataDao
import com.crtyiot.ept.data.Dao.TaskDao

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideTaskDao(appDatabase: AppDatabase): TaskDao {
        return appDatabase.taskDao()
    }

    @Provides
    fun provideGardenMaterialDao(appDatabase: AppDatabase): MaterialDao {
        return appDatabase.materialDao()
    }

    @Provides
    fun ScanDataDaoDao(appDatabase: AppDatabase): ScanDataDao {
        return appDatabase.scanDataDao()
    }

}
