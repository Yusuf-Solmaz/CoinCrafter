package com.yusuf.cryptocurrencytrading.ui.topLossGain.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.cryptocurrencytrading.data.retrofit.entity.CryptoCurrency
import com.yusuf.cryptocurrencytrading.data.retrofit.repository.CoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Collections
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class TopLossGainViewModel @Inject constructor(val repo: CoinRepository) : ViewModel() {

    var coins = MutableLiveData<List<CryptoCurrency>>()

    fun getAllCoins(positionArg: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.i("coins", repo.getAllCoins().toString())
            coins.postValue(repo.getAllCoins().data.cryptoCurrencyList)

            withContext(Dispatchers.Main) {
                val dataItem = coins.value!!

                Collections.sort(dataItem) { o1, o2 ->
                    (o2.quotes[0].percentChange24h.toInt())
                        .compareTo(o1.quotes[0].percentChange24h.toInt())
                }
                val list = ArrayList<CryptoCurrency>()

                if (positionArg == 0) {
                    list.clear()

                    for (i in 0 until min(10, dataItem.size)) {
                        list.add(dataItem[i])
                    }

                    coins.postValue(list)
                } else {
                    list.clear()
                    for (i in 0 until min(10, dataItem.size)) {
                        list.add(dataItem[dataItem.size - 1 - i])
                    }

                    coins.postValue(list)
                }
            }
        }
    }
}
