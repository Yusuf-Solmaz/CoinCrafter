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
class WalletViewModel @Inject constructor(val repo: CoinRepository, var auth: FirebaseAuth, val firestore: FirebaseFirestore,private val baseUser:BaseUserFirebase): ViewModel() {

    var balance = MutableLiveData<Double>()

    var coins = MutableLiveData<Coin>()

    var userCoinList = MutableLiveData<ArrayList<CryptoFirebase>>()


    fun getUserCoins() {
        viewModelScope.launch {
            try {
                Log.i("userCoin", baseUser.getUserCoins().toString())

                userCoinList.value = baseUser.getUserCoinsAsCryptoFirebase()

                Log.i("userCoinValue", userCoinList.value.toString())
            } catch (e: Exception) {
                println(e.localizedMessage)
            }
        }
    }


    fun getAllCoins(){
        viewModelScope.launch {

            coins.value = repo.getAllCoins()
        }

    }

    fun getBalance() {
        viewModelScope.launch {
            try {

                Log.i("balance",baseUser.getUserBalance().toString())

                balance.value = baseUser.getUserBalance()
                Log.i("balanceValue",balance.value.toString())

            }
            catch (e:Exception){
                println(e.localizedMessage)
            }
        }
    }

    fun addToBalance(amountToAdd: Double,view: View) {
        viewModelScope.launch {
            try {

                val currentBalance = baseUser.getUserBalance()
                val newBalance = currentBalance + amountToAdd


                firestore.collection("users").document(baseUser.getUserId())
                    .update("balance", newBalance)
                    .await()


                balance.value = newBalance

                Snackbar.make(view,"Adding Balance Successful",Snackbar.LENGTH_LONG).show()


            } catch (e: Exception) {
                Snackbar.make(view,"Adding Balance Not Successful",Snackbar.LENGTH_LONG).show()
            }
        }
    }

    fun checkBalance(amount: Double, view: View) {
        viewModelScope.launch {
            try {


                    val currentBalance = baseUser.getUserBalance()
                    val newBalance = currentBalance - amount

                    if (currentBalance == 0.0 ){
                        Toast.makeText(view.context,"Your balance is: 0.0",Toast.LENGTH_LONG).show()
                    }

                    else if (newBalance<0){
                        Toast.makeText(view.context,"You cannot withdraw more balance than the balance you have.",Toast.LENGTH_LONG).show()
                    }
                    else{
                        firestore.collection("users").document(baseUser.getUserId())
                            .update("balance", newBalance)
                            .await()

                        balance.value = newBalance

                        Snackbar.make(view,"Checking Balance Successful",Snackbar.LENGTH_LONG).show()
                    }




            } catch (e: Exception) {
                Snackbar.make(view,"Checking Balance Not Successful",Snackbar.LENGTH_LONG).show()
            }
        }
    }

    fun sellCrypto(coinName: String, sellAmount: Double, view: View, coins: List<CryptoCurrency>) {
        viewModelScope.launch {
            try {
                val userCoins = baseUser.getUserCoins()

                val existingCoin = userCoins.find { it["name"] == coinName }

                if (existingCoin != null) {
                    var existingAmount = existingCoin["amount"] as Double
                    var newAmount = existingAmount - sellAmount

                    val epsilon = 1e-50

                    if (newAmount > -epsilon && newAmount < epsilon) {
                        newAmount = 0.0
                    }

                    if (newAmount >= 0) {

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

                                val coinId = (existingCoin["id"]as Long).toInt()

                                val transaction = TransactionsFirebase(amountPrice,sellAmount,status = "Sold",name = coinName, id = coinId,date = Calendar.getInstance().time.toString())

                                firestore.collection("users").document(baseUser.getUserId())
                                    .update("transactions", FieldValue.arrayUnion(transaction))
                                    .await()
                            }
                        }

                        Snackbar.make(view, "Crypto sold successfully", Snackbar.LENGTH_LONG).show()
                    } else {
                        Snackbar.make(view, "The amount entered cannot be greater than the current amount.", Snackbar.LENGTH_LONG).show()
                    }
                } else {
                    Snackbar.make(view, "Crypto not found in user's wallet", Snackbar.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Snackbar.make(view, "Selling Crypto Failed: ${e.localizedMessage}", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    fun sellAllSelectedCrypto(coinName: String, view: View, coins: List<CryptoCurrency>){
        viewModelScope.launch {
            try {

                val userCoins = baseUser.getUserCoins()

                val existingCoin = userCoins.find { it["name"] == coinName }

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

                                val coinId = (existingCoin["id"]as Long).toInt()

                                val transaction = TransactionsFirebase(amountPrice,existingAmount,status = "Sold",name = coinName, id = coinId,date = Calendar.getInstance().time.toString())

                                firestore.collection("users").document(baseUser.getUserId())
                                    .update("transactions", FieldValue.arrayUnion(transaction))
                                    .await()

                            }
                        }

                        Snackbar.make(view, "Crypto sold successfully", Snackbar.LENGTH_LONG).show()
                    } else {
                        Snackbar.make(view, "The amount entered cannot be greater than the current amount.", Snackbar.LENGTH_LONG).show()
                    }
            }
             catch (e: Exception) {
                Snackbar.make(view, "Selling Crypto Failed: ${e.localizedMessage}", Snackbar.LENGTH_LONG).show()
            }
        }

    }




}