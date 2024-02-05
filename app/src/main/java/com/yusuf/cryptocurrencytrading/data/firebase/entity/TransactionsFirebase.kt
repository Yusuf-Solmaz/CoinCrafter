package com.yusuf.cryptocurrencytrading.data.firebase.entity

import java.io.Serializable

data class TransactionsFirebase (
    var amountPrice: Double,
    var amount: Double,
    val status: String,
    val name: String,
    val id: Int,
    val date: String
):Serializable