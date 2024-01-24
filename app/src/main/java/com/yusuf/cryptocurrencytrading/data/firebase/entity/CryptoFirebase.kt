package com.yusuf.cryptocurrencytrading.data.firebase.entity

import java.io.Serializable

data class CryptoFirebase(
    val name: String,
    val price: Double,
    val amount: Double
) : Serializable