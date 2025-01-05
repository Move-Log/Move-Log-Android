package com.ilgusu.data.di

import com.ilgusu.data.datasource.local.TokenLocalDataSource
import com.ilgusu.data.datasource.local.TokenLocalDataSourceImpl
import com.ilgusu.data.datasource.remote.AuthRemoteDataSource
import com.ilgusu.data.datasource.remote.AuthRemoteDataSourceImpl
import com.ilgusu.data.datasource.remote.RecordRemoteDataSource
import com.ilgusu.data.datasource.remote.RecordRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindTokenLocalDataSource(
        tokenLocalDataSourceImpl: TokenLocalDataSourceImpl
    ): TokenLocalDataSource

    @Binds
    @Singleton
    abstract fun bindAuthRemoteDataSource(
        authRemoteDataSourceImpl: AuthRemoteDataSourceImpl
    ): AuthRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindRecordRemoteDataSource(
        recordRemoteDataSourceImpl: RecordRemoteDataSourceImpl
    ): RecordRemoteDataSource
}
