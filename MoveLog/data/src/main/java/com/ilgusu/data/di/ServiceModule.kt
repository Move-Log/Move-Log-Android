package com.ilgusu.data.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal class ServiceModule {

//    @Provides
//    fun providesAlarmService(client: Retrofit): ExampleService =
//        client.create(ExampleService::class.java)
}