package com.yusuf.cryptocurrencytrading.ui.mainScreens.viewModel.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

open class BaseUserFirebase @Inject constructor(private val auth: FirebaseAuth, private val firestore: FirebaseFirestore) {

    private fun getUserId(): String{
        return auth.currentUser?.uid ?: ""
    }

    suspend fun getUserDocument() : DocumentSnapshot{
        return firestore.collection("users").document(getUserId()).get().await()
    }
}