package com.yusuf.cryptocurrencytrading.ui.onBoardingScreen

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.yusuf.cryptocurrencytrading.R

class SharedPreferenceManger(context: Context) {

    private val preference = context.getSharedPreferences(
        context.getString(R.string.app_name), AppCompatActivity.MODE_PRIVATE
    )

    private val editor = preference.edit()


    private val keyIsFirstTime = "isFirstTime"

    var isFirstTime
        get() = preference.getBoolean(keyIsFirstTime, true)
        set(value) {
            editor.putBoolean(keyIsFirstTime, value)
            editor.commit()
        }
}