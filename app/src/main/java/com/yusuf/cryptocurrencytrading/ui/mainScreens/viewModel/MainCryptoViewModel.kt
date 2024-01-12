package com.yusuf.cryptocurrencytrading.ui.mainScreens.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainCryptoViewModel: ViewModel() {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }


    fun getUserName(onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userId = auth.currentUser?.uid ?: ""
                val document = firestore.collection("users").document(userId).get().await()

                val username = if (document.exists()) {
                    document.getString("fullName") ?: ""
                } else {
                    ""
                }

                launch(Dispatchers.Main) {
                    if (username.isNotEmpty()) {
                        onSuccess(username)
                    } else {
                        onFailure("Username not found.")
                    }
                }
            } catch (e: Exception) {
                launch(Dispatchers.Main) {
                    onFailure("Error getting username: ${e.localizedMessage}")
                }
            }
        }
    }
}