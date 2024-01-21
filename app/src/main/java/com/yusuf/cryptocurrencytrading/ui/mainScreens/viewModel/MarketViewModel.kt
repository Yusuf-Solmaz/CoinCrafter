package com.yusuf.cryptocurrencytrading.ui.mainScreens.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.cryptocurrencytrading.data.retrofit.entity.Coin
import com.yusuf.cryptocurrencytrading.data.retrofit.repository.CoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketViewModel @Inject constructor(val repo: CoinRepository): ViewModel() {

    var coins = MutableLiveData<Coin>()

    fun getAllCoins(){
        viewModelScope.launch {
            Log.i("coins", repo.getAllCoins().toString())
            coins.value = repo.getAllCoins()
        }

    }
}