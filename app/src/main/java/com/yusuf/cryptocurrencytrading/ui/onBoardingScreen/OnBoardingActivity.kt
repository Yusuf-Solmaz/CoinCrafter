package com.yusuf.cryptocurrencytrading.ui.onBoardingScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yusuf.cryptocurrencytrading.R
import android.content.Intent
import android.os.Build
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yusuf.cryptocurrencytrading.MainActivity
import com.yusuf.cryptocurrencytrading.utils.NetworkUtils
import com.yusuf.cryptocurrencytrading.utils.gone
import com.yusuf.cryptocurrencytrading.utils.visible

class OnBoardingActivity : AppCompatActivity() {

    private val onBoardingPageChangeCallback = object : ViewPager2.OnPageChangeCallback(){
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            when (position){
                0 -> {
                    skipBtn.text = "Skip"
                    skipBtn.visible()

                }
                pagerList.size - 1 -> {
                    skipBtn.text = "Get Started"

                    skipBtn.visible()


                }
                else -> {
                    skipBtn.text = "Skip"
                    skipBtn.visible()

                }
            }
        }
    }


    private val pagerList = arrayListOf(
        Page("Get Success in the Crypto Art",R.drawable.crypto_image3,"Best way to invest & save money for the future just by using your phone","#202022"),
        Page("Discover the Power of Crypto",R.drawable.crypto_image2, "Cryptocurrencies are faster and more transparent than traditional investments, supporting international transactions.","#202022"),
        Page("Easy to Buy and Sell",R.drawable.crypto_image1,"Coin Crafters is a safe converter platform for buying, selling, transferring and storing crypto money.","#202022")

    )

    lateinit var onBoardingViewPager2 : ViewPager2
    lateinit var skipBtn : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        if (NetworkUtils.isInternetAvailable(this)) {

            onBoardingViewPager2= findViewById(R.id.onBoardingViewPager2)
            skipBtn= findViewById(R.id.skipBtn)


            onBoardingViewPager2.apply {
                adapter = OnBoardingAdapter(this@OnBoardingActivity,pagerList)
                registerOnPageChangeCallback(onBoardingPageChangeCallback)
                (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            }

            val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
            TabLayoutMediator(tabLayout,onBoardingViewPager2){tab, position -> }.attach()



            skipBtn.setOnClickListener {
                homeScreenIntent()
            }


        } else {
            NetworkUtils.showNoInternetDialog(this){
                finish()
            }

        }


    }

    override fun onDestroy() {
        onBoardingViewPager2.unregisterOnPageChangeCallback(onBoardingPageChangeCallback)
        super.onDestroy()
    }
    private fun homeScreenIntent() {
        val sharedPreferenceManger = SharedPreferenceManger(this)
        sharedPreferenceManger.isFirstTime = false
        val homeIntent = Intent(this,MainActivity::class.java)
        startActivity(homeIntent)
        finish()
    }
}