package com.yusuf.cryptocurrencytrading.ui.mainScreens.viewModel.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.yusuf.cryptocurrencytrading.data.firebase.entity.CryptoFirebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BaseUserFirebase @Inject constructor(private val auth: FirebaseAuth, private val firestore: FirebaseFirestore) {

     fun getUserId(): String{
        return auth.currentUser?.uid ?: ""
    }

    suspend fun getUserDocument() : DocumentSnapshot{
        return firestore.collection("users").document(getUserId()).get().await()
    }

    suspend fun getUserBalance(): Double{
       return getUserDocument().getDouble("balance") ?: 0.0
    }

    suspend fun getUserCoins(): List<HashMap<String, Any>> {
        val userCoins = getUserDocument().get("userCoin") as? List<HashMap<String, Any>>
        return userCoins ?: throw NullPointerException("User coins are null.")
    }

    suspend fun getUserFavs(): List<HashMap<String, Any>>? {
        return getUserDocument().get("favourites") as? List<HashMap<String, Any>>
    }

    suspend fun getUserName(): String{
        return getUserDocument().getString("username") ?: ""
    }

    suspend fun getUserTransactions(): List<HashMap<String, Any>>? {
        return getUserDocument().get("transactions") as? List<HashMap<String, Any>>
    }

    suspend fun getUserCoinsAsCryptoFirebase() : ArrayList<CryptoFirebase>?{
        return getUserDocument().get("userCoin") as ArrayList<CryptoFirebase>?
    }
}