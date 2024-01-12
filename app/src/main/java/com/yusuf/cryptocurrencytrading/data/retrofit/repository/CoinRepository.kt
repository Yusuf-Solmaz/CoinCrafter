package com.yusuf.cryptocurrencytrading.data.retrofit.repository

import com.yusuf.cryptocurrencytrading.data.retrofit.api.CoinsApi
import com.yusuf.cryptocurrencytrading.data.retrofit.entity.Coin
import com.yusuf.cryptocurrencytrading.data.retrofit.service.CoinService

class CoinRepository(val api: CoinsApi): CoinService {

    override suspend fun getAllCoins(): Coin {
        return api.getAllCoins()
    }
}