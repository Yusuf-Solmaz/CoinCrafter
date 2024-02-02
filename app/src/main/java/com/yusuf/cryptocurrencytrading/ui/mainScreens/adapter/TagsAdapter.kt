package com.yusuf.cryptocurrencytrading.ui.mainScreens.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yusuf.cryptocurrencytrading.R
import com.yusuf.cryptocurrencytrading.data.retrofit.entity.CryptoCurrency
import com.yusuf.cryptocurrencytrading.databinding.TagsRowBinding

class TagsAdapter (val context: Context, val coinTags: CryptoCurrency) : RecyclerView.Adapter<TagsAdapter.TagsHolder>()  {
    class TagsHolder(val binding: TagsRowBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsHolder {
        return TagsHolder(TagsRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return coinTags.tags.size
    }

    override fun onBindViewHolder(holder: TagsHolder, position: Int) {
        holder.binding.cardView.setBackgroundResource(R.drawable.coin_list_color)
        holder.binding.coinTag.text = coinTags.tags[position]
    }

}