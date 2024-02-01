package com.yusuf.cryptocurrencytrading.ui.mainScreens.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.yusuf.cryptocurrencytrading.data.retrofit.entity.Coin
import com.yusuf.cryptocurrencytrading.data.retrofit.repository.CoinRepository
import com.yusuf.cryptocurrencytrading.ui.mainScreens.viewModel.service.BaseUserFirebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCryptoViewModel @Inject constructor(val repo: CoinRepository,val auth: FirebaseAuth,val firestore: FirebaseFirestore,val baseUser: BaseUserFirebase): ViewModel() {

    var coins = MutableLiveData<Coin>()
    var loading = MutableLiveData<Boolean>()

    fun getUserName(onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                Log.i("userId",baseUser.getUserId())

                val document = baseUser.getUserDocument()

                val username = if (document.exists()) {
                    baseUser.getUserName()
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
        loading.value = true
        viewModelScope.launch {
            Log.i("coins", repo.getAllCoins().toString())
            coins.value = repo.getAllCoins()
            loading.value = false
        }

    }

    fun logOut(action: () -> Unit){
        auth.signOut()
        action()
    }
}