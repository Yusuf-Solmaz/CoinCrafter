package com.yusuf.cryptocurrencytrading.ui.onBoardingScreen

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.yusuf.cryptocurrencytrading.R

class OnBoardingFragment(private val page: Page) : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view =  inflater.inflate(R.layout.fragment_on_boarding, container, false)
        view.setBackgroundColor(Color.parseColor(page.color))
        val titleTxt = view.findViewById<TextView>(R.id.titleTxt)
        val descTxt = view.findViewById<TextView>(R.id.descTxt)
        val imageView = view.findViewById<ImageView>(R.id.imageView)

        titleTxt.text =  page.title
        descTxt.text =  page.desc
        imageView.setImageResource(page.image)
        return view
    }


}