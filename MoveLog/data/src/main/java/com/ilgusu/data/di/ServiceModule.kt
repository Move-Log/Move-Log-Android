package com.ilgusu.data.di

import android.content.Context
import com.ilgusu.data.service.KakaoAuthService
import com.ilgusu.data.service.RecordService
import com.ilgusu.data.service.auth.AuthService
import com.kakao.sdk.user.UserApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
internal class ServiceModule {

    @Provides
    fun providesKakaoAuthService(
        @ApplicationContext context: Context,
        client: UserApiClient = UserApiClient.instance
    ): KakaoAuthService = KakaoAuthService(context, client)

    @Provides
    fun providesAuthService(
        client: Retrofit
    ): AuthService = client.create(AuthService::class.java)

    @Provides
    fun providesRecordService(
        client: Retrofit
    ): RecordService = client.create(RecordService::class.java)
}