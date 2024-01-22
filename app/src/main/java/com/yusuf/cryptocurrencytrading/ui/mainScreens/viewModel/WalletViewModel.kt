package com.yusuf.cryptocurrencytrading.ui.mainScreens.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.yusuf.cryptocurrencytrading.data.retrofit.repository.CoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(val repo: CoinRepository): ViewModel() {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    var balance = MutableLiveData<Double>()

    fun getBalance() {
        viewModelScope.launch {
            try {
                val userId = auth.currentUser?.uid ?: ""
                val document = firestore.collection("users").document(userId).get().await()

                Log.i("balance",document.getDouble("balance").toString())

                balance.value = document.getDouble("balance")
                Log.i("balanceValue",balance.value.toString())


            }
            catch (e:Exception){
                println(e.localizedMessage)
            }
        }
    }
}