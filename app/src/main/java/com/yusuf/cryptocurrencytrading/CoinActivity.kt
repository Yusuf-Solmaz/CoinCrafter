package com.yusuf.cryptocurrencytrading

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.yusuf.cryptocurrencytrading.databinding.ActivityCoinBinding
import com.yusuf.cryptocurrencytrading.utils.NetworkUtils
import com.yusuf.cryptocurrencytrading.utils.NetworkUtils.showNoInternetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoinActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCoinBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment

            NavigationUI.setupWithNavController(binding.bottomNavView,navHostFragment.navController)
        } else {

            showNoInternetDialog(this){
                finish()
            }

        }


    }

    fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}