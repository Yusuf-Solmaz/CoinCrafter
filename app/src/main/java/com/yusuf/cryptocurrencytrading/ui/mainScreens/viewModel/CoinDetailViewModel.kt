package com.yusuf.cryptocurrencytrading.ui.mainScreens.viewModel


import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.yusuf.cryptocurrencytrading.data.retrofit.entity.CryptoCurrency
import com.yusuf.cryptocurrencytrading.data.retrofit.repository.CoinRepository
import com.yusuf.cryptocurrencytrading.utils.Utils.Companion.toCryptoFirebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class CoinDetailViewModel @Inject constructor(val repo: CoinRepository): ViewModel() {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }



    fun buyCrypto(amountPrice: Double, crypto: CryptoCurrency, view: View) {
        viewModelScope.launch {
            val currentBalance = getUserDocument()?.getDouble("balance") ?: 0.0

            if (amountPrice > currentBalance) {
                Snackbar.make(view, "Not Enough Balance", Snackbar.LENGTH_LONG).show()
            } else {
                try {
                    val userCoinAmount = amountPrice / crypto.quotes[0].price
                    val boughtCoin = crypto.toCryptoFirebase(userCoinAmount)

                    val userCoins = getUserDocument()?.get("userCoin") as? List<HashMap<String, Any>>

                    if (userCoins != null) {

                        val existingCoin = userCoins.find { it["name"] == boughtCoin.name }
                        if (existingCoin != null) {
                            val existingAmount = existingCoin["amount"] as Double
                            val newAmount = existingAmount + boughtCoin.amount

                            firestore.collection("users").document(getUserId())
                                .update("userCoin", FieldValue.arrayRemove(existingCoin))
                                .await()

                            firestore.collection("users").document(getUserId())
                                .update("userCoin", FieldValue.arrayUnion(boughtCoin.copy(amount = newAmount)))
                                .await()

                            Snackbar.make(view, "${amountPrice}$ worth of ${crypto.name} was purchased.", Snackbar.LENGTH_LONG).show()

                        } else {

                            firestore.collection("users").document(getUserId())
                                .update("userCoin", FieldValue.arrayUnion(boughtCoin))
                                .await()

                            Snackbar.make(view, "${amountPrice}$ worth of ${crypto.name} was purchased.", Snackbar.LENGTH_LONG).show()
                        }
                    }

                    val newBalance = currentBalance - amountPrice

                    firestore.collection("users").document(getUserId())
                        .update("balance", newBalance)
                        .await()

                } catch (e: Exception) {
                    println(e.localizedMessage)
                }
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