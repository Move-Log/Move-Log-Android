package com.ilgusu.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
internal class ServiceModule {

//    @Provides
//    fun providesAlarmService(client: Retrofit): ExampleService =
//        client.create(ExampleService::class.java)
}