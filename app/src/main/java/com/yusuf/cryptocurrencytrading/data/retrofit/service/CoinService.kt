package com.yusuf.cryptocurrencytrading.data.retrofit.service

import com.yusuf.cryptocurrencytrading.data.retrofit.entity.Coin

interface CoinService {

    suspend fun getAllCoins() : Coin
}