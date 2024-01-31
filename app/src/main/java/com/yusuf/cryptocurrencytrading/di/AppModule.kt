package com.yusuf.cryptocurrencytrading.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.yusuf.cryptocurrencytrading.data.retrofit.api.CoinsApi
import com.yusuf.cryptocurrencytrading.data.retrofit.repository.CoinRepository
import com.yusuf.cryptocurrencytrading.ui.mainScreens.viewModel.service.BaseUserFirebase
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

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()


    @Provides
    @Singleton
    fun provideBaseUserFirebase(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): BaseUserFirebase {
        return BaseUserFirebase(auth, firestore)
    }
}