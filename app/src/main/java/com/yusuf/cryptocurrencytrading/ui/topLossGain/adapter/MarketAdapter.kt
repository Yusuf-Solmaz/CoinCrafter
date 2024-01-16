package com.yusuf.cryptocurrencytrading.ui.topLossGain.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yusuf.cryptocurrencytrading.R
import com.yusuf.cryptocurrencytrading.data.retrofit.entity.CryptoCurrency
import com.yusuf.cryptocurrencytrading.databinding.CoinListCardviewBinding

class MarketAdapter(var context: Context, var coinList: List<CryptoCurrency>) : RecyclerView.Adapter<MarketAdapter.RecyclerViewHolder>() {

    class RecyclerViewHolder(val binding: CoinListCardviewBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val itemBinding = CoinListCardviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RecyclerViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return coinList.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val coin = coinList[position]

        holder.binding.cardView.setBackgroundResource(R.drawable.coin_list_color)

        holder.binding.coinName.text = coin.name

        holder.binding.coinValue.text = coin.quotes[0].price.toString()

        Glide.with(context).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/${coin.id}.png"
        ).placeholder(R.drawable.baseline_currency_bitcoin_24).into(holder.binding.coinImg)

        Glide.with(context).load(
            "https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/${coin.id}.png"
        ).placeholder(R.drawable.baseline_currency_bitcoin_24).into(holder.binding.coinGraphImg)


        if (0 > coin.quotes[0].percentChange24h){
            holder.binding.coinRatio.setTextColor(ContextCompat.getColor(context, R.color.red))
            holder.binding.coinRatio.text = "${String.format("%.02f",coin.quotes[0].percentChange24h)}%"
        } else {
            holder.binding.coinRatio.setTextColor(ContextCompat.getColor(context, R.color.green))
            holder.binding.coinRatio.text = "${String.format("%.02f",coin.quotes[0].percentChange24h)}%"
        }
    }
}