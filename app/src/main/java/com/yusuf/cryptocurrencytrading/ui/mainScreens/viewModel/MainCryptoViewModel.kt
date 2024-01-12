package com.yusuf.cryptocurrencytrading.ui.mainScreens.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.yusuf.cryptocurrencytrading.data.retrofit.entity.Coin
import com.yusuf.cryptocurrencytrading.data.retrofit.repository.CoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class MainCryptoViewModel @Inject constructor(val repo: CoinRepository): ViewModel() {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    var coins = MutableLiveData<Coin>()


    fun getUserName(onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.i("userId",auth.currentUser?.uid.toString())
                val userId = auth.currentUser?.uid ?: ""
                val document = firestore.collection("users").document(userId).get().await()

                val username = if (document.exists()) {
                    document.getString("username") ?: ""
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

    fun getAllCoins(){
        viewModelScope.launch {
            Log.i("coins", repo.getAllCoins().toString())
            coins.value = repo.getAllCoins()
        }

    }

    fun logOut(action: () -> Unit){
        auth.signOut()
        action()
    }
}