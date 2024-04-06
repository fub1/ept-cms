package com.crtyiot.ept.di

import com.crtyiot.ept.network.MaterialApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideMaterialApiService(): MaterialApiService {
        return MaterialApiService.create()
    }
}
