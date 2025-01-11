package com.ilgusu.data.di

import com.ilgusu.data.service.RecordService
import com.ilgusu.data.service.auth.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
internal class ServiceModule {

    @Provides
    fun providesAuthService(
        client: Retrofit
    ): AuthService = client.create(AuthService::class.java)

    @Provides
    fun providesRecordService(
        client: Retrofit
    ): RecordService = client.create(RecordService::class.java)
}