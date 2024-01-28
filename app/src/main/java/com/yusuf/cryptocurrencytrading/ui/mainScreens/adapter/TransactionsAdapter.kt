package com.yusuf.cryptocurrencytrading.ui.mainScreens.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yusuf.cryptocurrencytrading.R
import com.yusuf.cryptocurrencytrading.data.firebase.entity.TransactionsFirebase
import com.yusuf.cryptocurrencytrading.databinding.TransactionsCardviewRowBinding
import java.text.SimpleDateFormat
import java.util.Locale

class TransactionsAdapter(val context: Context,val transactions:List<TransactionsFirebase>) : RecyclerView.Adapter<TransactionsAdapter.TransactionsHolder>(){
    class TransactionsHolder(val binding: TransactionsCardviewRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionsHolder {
        return TransactionsHolder(TransactionsCardviewRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onBindViewHolder(holder: TransactionsHolder, position: Int) {

        holder.binding.cardView.setBackgroundResource(R.drawable.coin_list_color)

        val transaction = transactions[position]

        val inputDate = transaction.date
        val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH)

        val date = inputFormat.parse(inputDate)
        val formattedDate = outputFormat.format(date)


        Glide.with(context).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/${transaction.id}.png"
        ).placeholder(R.drawable.baseline_currency_bitcoin_24).into(holder.binding.coinImg)

        holder.binding.coinName.text = transaction.name

        holder.binding.transactionAmount.text = "${String.format("%.5f",transaction.amount)}"

        if (transaction.status == "Sold"){
            holder.binding.transactionStatus.setTextColor(ContextCompat.getColor(context, R.color.red))
            holder.binding.transactionStatus.text = transaction.status
        }
        else{
            holder.binding.transactionStatus.setTextColor(ContextCompat.getColor(context, R.color.green))
            holder.binding.transactionStatus.text = transaction.status
        }

        holder.binding.transactionsDate.text = formattedDate
    }
}