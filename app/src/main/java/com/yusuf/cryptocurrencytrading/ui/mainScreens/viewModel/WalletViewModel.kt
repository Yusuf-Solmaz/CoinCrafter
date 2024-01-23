package com.yusuf.cryptocurrencytrading.ui.mainScreens.viewModel

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
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

                if (getUserDocument() != null){
                    Log.i("balance",getUserDocument()!!.getDouble("balance").toString())

                    balance.value = getUserDocument()!!.getDouble("balance")
                    Log.i("balanceValue",balance.value.toString())

                }

            }
            catch (e:Exception){
                println(e.localizedMessage)
            }
        }
    }

    fun addToBalance(amountToAdd: Double,view: View) {
        viewModelScope.launch {
            try {

                if (getUserDocument() != null){
                    val currentBalance = getUserDocument()!!.getDouble("balance") ?: 0.0
                    val newBalance = currentBalance + amountToAdd


                    firestore.collection("users").document(getUserId())
                        .update("balance", newBalance)
                        .await()


                    balance.value = newBalance

                    Snackbar.make(view,"Adding Balance Successful",Snackbar.LENGTH_LONG).show()
                }


            } catch (e: Exception) {
                Snackbar.make(view,"Adding Balance Not Successful",Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun getUserId(): String{
        return auth.currentUser?.uid ?: ""
    }

    private suspend fun getUserDocument(): DocumentSnapshot? {
        return firestore.collection("users").document(getUserId()).get().await()
    }

}