package com.ilgusu.data.di

import android.content.Context
import com.ilgusu.data.service.KakaoAuthService
import com.kakao.sdk.user.UserApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal class ServiceModule {

    @Provides
    fun providesKakaoAuthService(
        @ApplicationContext context: Context,
        client: UserApiClient = UserApiClient.instance
    ): KakaoAuthService = KakaoAuthService(context, client)

}