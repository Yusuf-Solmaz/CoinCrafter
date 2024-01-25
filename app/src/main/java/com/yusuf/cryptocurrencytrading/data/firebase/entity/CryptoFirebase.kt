package com.yusuf.cryptocurrencytrading.data.firebase.entity

import java.io.Serializable

data class CryptoFirebase(
    var amount: Double,
    val price: Double,
    val name: String,
    val id: Int
) : Serializable