package com.yusuf.cryptocurrencytrading.ui.mainScreens.viewModel

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.yusuf.cryptocurrencytrading.data.firebase.entity.CryptoFirebase
import com.yusuf.cryptocurrencytrading.data.firebase.entity.FavouriteCryptosFirebase
import com.yusuf.cryptocurrencytrading.data.firebase.entity.TransactionsFirebase
import com.yusuf.cryptocurrencytrading.data.retrofit.entity.CryptoCurrency
import com.yusuf.cryptocurrencytrading.ui.mainScreens.viewModel.service.BaseUserFirebase
import com.yusuf.cryptocurrencytrading.utils.Utils.Companion.toCryptoFirebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    private val baseUserFirebase: BaseUserFirebase,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    var isFavourite = MutableLiveData<Boolean>()

    fun buyCrypto(amountPrice: Double, crypto: CryptoCurrency, view: View) {
        viewModelScope.launch {
            try {
                val currentBalance = baseUserFirebase.getUserBalance()

                if (amountPrice > currentBalance) {
                    Snackbar.make(view, "Not Enough Balance", Snackbar.LENGTH_LONG).show()
                } else {
                    handleCryptoPurchase(amountPrice, crypto, view)
                }
            } catch (e: Exception) {
                Log.e("CoinDetailViewModel", "Error buying crypto: ${e.localizedMessage}")
            }
        }
    }

    private suspend fun handleCryptoPurchase(amountPrice: Double, crypto: CryptoCurrency, view: View) {
        val userCoinAmount = amountPrice / crypto.quotes[0].price
        val boughtCoin = crypto.toCryptoFirebase(userCoinAmount)

        val userCoins = baseUserFirebase.getUserCoins()
        val existingCoin = userCoins.find { it["name"] == boughtCoin.name }

        updateFirebaseUserCoins(existingCoin, boughtCoin)

        val transaction = TransactionsFirebase(
            amountPrice, boughtCoin.amount, status = "Purchased",
            name = crypto.name, id = crypto.id, date = Calendar.getInstance().time.toString()
        )

        updateFirebaseTransactions(transaction)

        val newBalance = baseUserFirebase.getUserBalance() - amountPrice
        updateFirebaseUserBalance(newBalance)

        Snackbar.make(view, "${amountPrice}$ worth of ${crypto.name} was purchased.", Snackbar.LENGTH_LONG).show()
    }

    private suspend fun updateFirebaseUserCoins(existingCoin: HashMap<String, Any>?, boughtCoin: CryptoFirebase) {
        if (existingCoin != null) {
            val existingAmount = existingCoin["amount"] as Double
            val newAmount = existingAmount + boughtCoin.amount

            firestore.collection("users").document(baseUserFirebase.getUserId())
                .update("userCoin", FieldValue.arrayRemove(existingCoin))
                .await()

            firestore.collection("users").document(baseUserFirebase.getUserId())
                .update("userCoin", FieldValue.arrayUnion(boughtCoin.copy(amount = newAmount)))
                .await()
        } else {
            firestore.collection("users").document(baseUserFirebase.getUserId())
                .update("userCoin", FieldValue.arrayUnion(boughtCoin))
                .await()
        }
    }

    private suspend fun updateFirebaseTransactions(transaction: TransactionsFirebase) {
        firestore.collection("users").document(baseUserFirebase.getUserId())
            .update("transactions", FieldValue.arrayUnion(transaction))
            .await()
    }

    private suspend fun updateFirebaseUserBalance(newBalance: Double) {
        firestore.collection("users").document(baseUserFirebase.getUserId())
            .update("balance", newBalance)
            .await()
    }

    fun addToFavourites(crypto: CryptoCurrency) {
        viewModelScope.launch {
            val favCoin = FavouriteCryptosFirebase(crypto.name, crypto.id)

            firestore.collection("users").document(baseUserFirebase.getUserId())
                .update("favourites", FieldValue.arrayUnion(favCoin))
                .await()

            isFavourite.value = true
        }
    }

    fun deleteFromFavourites(crypto: CryptoCurrency) {
        viewModelScope.launch {
            try {
                val favCoin = FavouriteCryptosFirebase(crypto.name, crypto.id)

                firestore.collection("users").document(baseUserFirebase.getUserId())
                    .update("favourites", FieldValue.arrayRemove(favCoin))
                    .await()

                isFavourite.value = false
            } catch (e: Exception) {
                Log.e("CoinDetailFav", "Error deleting from favourites: ${e.localizedMessage}")
            }
        }
    }

    fun isFavourite(crypto: CryptoCurrency) {
        viewModelScope.launch {
            try {
                val favourites = baseUserFirebase.getUserFavs()
                isFavourite.value = favourites?.any {
                    it["id"].toString() == crypto.id.toString()
                } ?: false
            } catch (e: Exception) {
                Log.e("CoinDetailViewModel", "Error checking if crypto is favorite: ${e.localizedMessage}")
                isFavourite.value = false
            }
        }
    }
}
