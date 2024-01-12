package com.yusuf.cryptocurrencytrading.di

import com.yusuf.cryptocurrencytrading.data.retrofit.api.CoinsApi
import com.yusuf.cryptocurrencytrading.data.retrofit.repository.CoinRepository
import com.yusuf.cryptocurrencytrading.utils.Utils.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideCoinsRepository(api: CoinsApi): CoinRepository{
        return CoinRepository(api)
    }

    @Provides
    @Singleton
    fun provideCoinsApi() : CoinsApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CoinsApi::class.java)
    }
}