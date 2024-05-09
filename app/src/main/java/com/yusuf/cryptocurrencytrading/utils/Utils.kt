package com.yusuf.cryptocurrencytrading.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.yusuf.cryptocurrencytrading.data.firebase.entity.CryptoFirebase
import com.yusuf.cryptocurrencytrading.data.retrofit.entity.CryptoCurrency

class Utils{
    companion object{
        private const val emailRegex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"
        private const val nameRegex = "^[a-zA-ZüÜğĞıİşŞöÖçÇ]+$"
        const val PRIVACY_POLICY_URL = "https://doc-hosting.flycricket.io/coin-crafter-privacy-policy/32863c2b-4a9e-4566-9a4d-6d9e5f14daff/privacy"
        const val TERMS_OF_USE_URL = "https://doc-hosting.flycricket.io/coin-crafter-terms-of-use/0eb4464f-eb89-4ee5-a9c0-de12b9921929/terms"

        fun  emailAndPasswordControl(email:String,password:String,context: Context) :Boolean{
            return if (email.equals("") || password.equals("")) {
                Toast.makeText(context, "E-mail or Password can not be empty.", Toast.LENGTH_LONG).show();
                false
            } else if (!email.matches(emailRegex.toRegex())) {
                Toast.makeText(context, "Please write your email in the correct format.", Toast.LENGTH_LONG).show();
                false;
            } else {
                true;
            }
        }

        fun nameControl(name: String, context: Context): Boolean {
            if (name.isEmpty()) {
                Toast.makeText(context, "Name can not be empty!", Toast.LENGTH_LONG).show()
                return false
            }

            return if (name.matches(nameRegex.toRegex())) {
                true
            } else {
                Toast.makeText(context, "Invalid name format!", Toast.LENGTH_LONG).show()
                false
            }
        }

        const val BASE_URL ="https://api.coinmarketcap.com/"
        const val GET_COINS = "data-api/v3/cryptocurrency/listing?start1&limit=500"

        fun CryptoCurrency.toCryptoFirebase(amount:Double) : CryptoFirebase{
            return CryptoFirebase(
                amount,quotes[0].price, name,id
            )
        }

    }
}

fun View.invisible(){
    visibility = View.INVISIBLE
}

fun View.visible(){
    visibility = View.VISIBLE
}

fun View.gone(){
    visibility = View.GONE
}

