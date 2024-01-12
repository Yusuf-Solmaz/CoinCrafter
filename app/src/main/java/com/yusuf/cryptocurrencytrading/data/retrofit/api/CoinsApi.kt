package com.yusuf.cryptocurrencytrading.data.retrofit.api

import com.yusuf.cryptocurrencytrading.data.retrofit.entity.Coin
import com.yusuf.cryptocurrencytrading.utils.Utils.Companion.GET_COINS
import retrofit2.http.GET

interface CoinsApi {

    @GET(GET_COINS)
    suspend fun getAllCoins() : Coin
}