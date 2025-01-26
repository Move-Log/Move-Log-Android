package com.ilgusu.data.di

import com.ilgusu.data.repository.AuthRepositoryImpl
import com.ilgusu.data.repository.NewsRepositoryImpl
import com.ilgusu.data.repository.RecordRepositoryImpl
import com.ilgusu.data.repository.TokenRepositoryImpl
import com.ilgusu.domain.repository.AuthRepository
import com.ilgusu.domain.repository.NewsRepository
import com.ilgusu.domain.repository.RecordRepository
import com.ilgusu.domain.repository.TokenRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsTokenRepository(impl: TokenRepositoryImpl): TokenRepository

    @Binds
    @Singleton
    abstract fun bindsAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindsRecordRepository(impl: RecordRepositoryImpl): RecordRepository

    @Binds
    @Singleton
    abstract fun bindsNewsRepository(impl: NewsRepositoryImpl): NewsRepository
}