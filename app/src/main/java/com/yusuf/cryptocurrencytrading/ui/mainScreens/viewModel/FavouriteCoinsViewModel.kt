package com.yusuf.cryptocurrencytrading.ui.mainScreens.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.cryptocurrencytrading.data.firebase.entity.FavouriteCryptosFirebase
import com.yusuf.cryptocurrencytrading.data.retrofit.entity.Coin
import com.yusuf.cryptocurrencytrading.data.retrofit.repository.CoinRepository
import com.yusuf.cryptocurrencytrading.ui.mainScreens.viewModel.service.BaseUserFirebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteCoinsViewModel @Inject constructor(val repo: CoinRepository, private val baseUser: BaseUserFirebase): ViewModel() {

    val favCoins = MutableLiveData<List<FavouriteCryptosFirebase>>()
    var coins = MutableLiveData<Coin>()


    fun getFavouriteCryptos() {
        viewModelScope.launch {
            try {

                val favourites = baseUser.getUserFavs()

                favCoins.value = favourites?.map {
                    FavouriteCryptosFirebase(it["name"] as String, (it["id"] as Long).toInt())
                } ?: emptyList()
            } catch (e: Exception) {
                Log.e("FavouriteCoinsViewModel", "Error getting favourite cryptos: ${e.localizedMessage}")
                favCoins.value = emptyList()
            }
        }
    }

    fun getAllCoins(){
        viewModelScope.launch {
            coins.value = repo.getAllCoins()
        }
    }
}