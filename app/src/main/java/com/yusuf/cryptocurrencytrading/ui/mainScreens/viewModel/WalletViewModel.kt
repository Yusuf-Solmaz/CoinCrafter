package com.yusuf.cryptocurrencytrading.ui.mainScreens.viewModel

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.yusuf.cryptocurrencytrading.data.firebase.entity.CryptoFirebase
import com.yusuf.cryptocurrencytrading.data.firebase.entity.TransactionsFirebase
import com.yusuf.cryptocurrencytrading.data.retrofit.entity.Coin
import com.yusuf.cryptocurrencytrading.data.retrofit.entity.CryptoCurrency
import com.yusuf.cryptocurrencytrading.data.retrofit.repository.CoinRepository
import com.yusuf.cryptocurrencytrading.ui.mainScreens.viewModel.service.BaseUserFirebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val repo: CoinRepository,
    private val firestore: FirebaseFirestore,
    private val baseUser: BaseUserFirebase
) : ViewModel() {

    var balance = MutableLiveData<Double>()
    var coins = MutableLiveData<Coin>()
    var userCoinList = MutableLiveData<ArrayList<CryptoFirebase>>()
    var loadingCoins = MutableLiveData<Boolean>()
    var loadingBalance = MutableLiveData<Boolean>()

    fun getUserCoins() {
        loadingCoins.value = true
        viewModelScope.launch {
            try {
                Log.i("userCoin", baseUser.getUserCoins().toString())
                userCoinList.value = baseUser.getUserCoinsAsCryptoFirebase()
                Log.i("userCoinValue", userCoinList.value.toString())
                loadingCoins.value = false
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun getAllCoins() {
        viewModelScope.launch {
            coins.value = repo.getAllCoins()
        }
    }

    fun getBalance() {
        loadingBalance.value = true
        viewModelScope.launch {
            try {
                Log.i("balance", baseUser.getUserBalance().toString())
                balance.value = baseUser.getUserBalance()
                Log.i("balanceValue", balance.value.toString())
                loadingBalance.value = false
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    fun addToBalance(amountToAdd: Double, view: View) {
        viewModelScope.launch {
            try {
                val currentBalance = baseUser.getUserBalance()
                val newBalance = currentBalance + amountToAdd

                updateFirebaseBalance(newBalance)

                balance.value = newBalance
                showSnackbar(view, "Adding Balance Successful")
            } catch (e: Exception) {
                showSnackbar(view, "Adding Balance Not Successful")
            }
        }
    }

    fun checkBalance(amount: Double, view: View) {
        viewModelScope.launch {
            try {
                val currentBalance = baseUser.getUserBalance()
                val newBalance = currentBalance - amount

                handleCheckBalance(view, currentBalance, newBalance)
            } catch (e: Exception) {
                showSnackbar(view, "Checking Balance Not Successful")
            }
        }
    }

    private fun handleCheckBalance(view: View, currentBalance: Double, newBalance: Double) {
        if (currentBalance == 0.0) {
            Toast.makeText(view.context, "Your balance is: 0.0", Toast.LENGTH_LONG).show()
        } else if (newBalance < 0) {
            Toast.makeText(
                view.context,
                "You cannot withdraw more balance than the balance you have.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            updateFirebaseBalance(newBalance)
            balance.value = newBalance
            showSnackbar(view, "Checking Balance Successful")
        }
    }

    fun sellCrypto(coinName: String, sellAmount: Double, view: View, coins: List<CryptoCurrency>) {
        viewModelScope.launch {
            try {
                val userCoins = baseUser.getUserCoins()
                val existingCoin = userCoins.find { it["name"] == coinName }

                handleSellCrypto(existingCoin, sellAmount, view, coins)
            } catch (e: Exception) {
                showSnackbar(view, "Selling Crypto Failed: ${e.localizedMessage}")
            }
        }
    }

    private suspend fun handleSellCrypto(
        existingCoin: HashMap<String, Any>?,
        sellAmount: Double,
        view: View,
        coins: List<CryptoCurrency>
    ) {
        if (existingCoin != null) {
            var existingAmount = existingCoin["amount"] as Double
            var newAmount = existingAmount - sellAmount

            handleNewAmount(existingCoin, newAmount, sellAmount, view, coins)
        } else {
            showSnackbar(view, "Crypto not found in user's wallet")
        }
    }

    private suspend fun handleNewAmount(
        existingCoin: HashMap<String, Any>,
        originalAmount: Double,
        sellAmount: Double,
        view: View,
        coins: List<CryptoCurrency>
    ) {
        val epsilon = 1e-50
        var newAmount = originalAmount

        if (newAmount > -epsilon && newAmount < epsilon) {
            newAmount = 0.0
        }

        if (newAmount >= 0) {
            updateFirebaseUserCoins(existingCoin, newAmount)
            handleUpdatedCoin(existingCoin, newAmount, sellAmount, view, coins)
        } else {
            showSnackbar(view, "The amount entered cannot be greater than the current amount.")
        }
    }

    private suspend fun handleUpdatedCoin(
        existingCoin: HashMap<String, Any>,
        newAmount: Double,
        sellAmount: Double,
        view: View,
        coins: List<CryptoCurrency>
    ) {
        firestore.collection("users").document(baseUser.getUserId())
            .update("userCoin", FieldValue.arrayRemove(existingCoin))
            .await()

        if (newAmount > epsilon) {
            val updatedCoin = existingCoin.toMutableMap()
            updatedCoin["amount"] = newAmount

            firestore.collection("users").document(baseUser.getUserId())
                .update("userCoin", FieldValue.arrayUnion(updatedCoin))
                .await()
        }

        getUserCoins()

        for (data in coins) {
            if (data.name != existingCoin["name"] as String) {
                continue
            } else {
                addToBalance(sellAmount * data.quotes[0].price, view)
                val amountPrice = sellAmount * data.quotes[0].price

                val coinId = (existingCoin["id"] as Long).toInt()

                handleTransaction(amountPrice, sellAmount, coinId, view, data)
            }
        }

        showSnackbar(view, "Crypto sold successfully")
    }

    private suspend fun handleTransaction(
        amountPrice: Double,
        sellAmount: Double,
        coinId: Int,
        view: View,
        data: CryptoCurrency
    ) {
        val transaction = TransactionsFirebase(
            amountPrice,
            sellAmount,
            status = "Sold",
            name = data.name,
            id = coinId,
            date = Calendar.getInstance().time.toString()
        )

        firestore.collection("users").document(baseUser.getUserId())
            .update("transactions", FieldValue.arrayUnion(transaction))
            .await()
    }

    fun sellAllSelectedCrypto(coinName: String, view: View, coins: List<CryptoCurrency>) {
        viewModelScope.launch {
            try {
                val userCoins = baseUser.getUserCoins()
                val existingCoin = userCoins.find { it["name"] == coinName }

                handleSellAllSelectedCrypto(existingCoin, view, coins)
            } catch (e: Exception) {
                showSnackbar(view, "Selling Crypto Failed: ${e.localizedMessage}")
            }
        }
    }

    private suspend fun handleSellAllSelectedCrypto(
        existingCoin: HashMap<String, Any>?,
        view: View,
        coins: List<CryptoCurrency>
    ) {
        if (existingCoin != null) {
            val existingAmount = existingCoin["amount"] as Double
            val coinName = existingCoin["name"] as String

            firestore.collection("users").document(baseUser.getUserId())
                .update("userCoin", FieldValue.arrayRemove(existingCoin))
                .await()

            getUserCoins()

            for (data in coins) {
                if (data.name != existingCoin["name"] as String) {
                    continue
                } else {
                    addToBalance(existingAmount * data.quotes[0].price, view)

                    val amountPrice = existingAmount * data.quotes[0].price
                    val coinId = (existingCoin["id"] as Long).toInt()

                    handleTransaction(amountPrice, existingAmount, coinId, view, data)
                }
            }

            showSnackbar(view, "Crypto sold successfully")
        } else {
            showSnackbar(view, "The amount entered cannot be greater than the current amount.")
        }
    }

    private fun updateFirebaseBalance(newBalance: Double) {
        viewModelScope.launch {
            firestore.collection("users").document(baseUser.getUserId())
                .update("balance", newBalance)
                .await()
        }
    }

    private suspend fun updateFirebaseUserCoins(existingCoin: HashMap<String, Any>, newAmount: Double) {
        firestore.collection("users").document(baseUser.getUserId())
            .update("userCoin", FieldValue.arrayRemove(existingCoin))
            .await()

        if (newAmount > epsilon) {
            val updatedCoin = existingCoin.toMutableMap()
            updatedCoin["amount"] = newAmount
            firestore.collection("users").document(baseUser.getUserId())
                .update("userCoin", FieldValue.arrayUnion(updatedCoin))
                .await()
        }
    }

    private fun showSnackbar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    private fun handleException(e: Exception) {
        println(e.localizedMessage)
    }

    companion object {
        private const val epsilon = 1e-50
    }
}
