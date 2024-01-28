package com.yusuf.cryptocurrencytrading.ui.mainScreens.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.yusuf.cryptocurrencytrading.data.firebase.entity.TransactionsFirebase
import com.yusuf.cryptocurrencytrading.data.retrofit.repository.CoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(val repo: CoinRepository): ViewModel() {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    var transactions = MutableLiveData<List<TransactionsFirebase>>()

    fun getAllTransactions() {
        viewModelScope.launch {
            try {
                val userId = getUserId()
                val documentSnapshot = firestore.collection("users").document(userId).get().await()

                if (documentSnapshot.exists()) {
                    val transactionsList = documentSnapshot.get("transactions") as? List<HashMap<String, Any>>

                    val transactionDataList = mutableListOf<TransactionsFirebase>()

                    transactionsList?.let {
                        for (transaction in it) {
                            val amountPrice = (transaction["amountPrice"] as? Double) ?: 0.0
                            val amount = (transaction["amount"] as? Double) ?: 0.0
                            val status = transaction["status"] as? String ?: ""
                            val name = transaction["name"] as? String ?: ""
                            val id = (transaction["id"] as? Long)?.toInt() ?: 0
                            val date = transaction["date"] as? String ?: ""
                            //val date = transaction["date"] as? Date ?: Calendar.getInstance().time

                            val transactionData = TransactionsFirebase(
                                amountPrice = amountPrice,
                                amount = amount,
                                status = status,
                                name = name,
                                id = id,
                                date = date
                            )

                            transactionDataList.add(transactionData)
                        }
                    }

                    transactions.postValue(transactionDataList)

                    Log.i("transactionsData", transactions.toString())
                } else {
                    Log.e("TransactionsViewModel", "User not found or transactions field missing.")
                }
            } catch (e: Exception) {
                Log.e("TransactionsViewModel", "Error getting transactions: ${e.localizedMessage}")
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