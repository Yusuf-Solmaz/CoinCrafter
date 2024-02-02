package com.yusuf.cryptocurrencytrading.ui.onBoardingScreen

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import com.yusuf.cryptocurrencytrading.MainActivity
import com.yusuf.cryptocurrencytrading.R

class BrandActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brand)



        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }


    override fun onStart() {
        super.onStart()
        isFirstTime()
    }

    private fun isFirstTime(){
        Handler(Looper.getMainLooper()).postDelayed({
            val sharedPreferenceManger = SharedPreferenceManger(this)
            if (sharedPreferenceManger.isFirstTime) {
                startActivity(Intent(this, OnBoardingActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        },2000)
    }
}