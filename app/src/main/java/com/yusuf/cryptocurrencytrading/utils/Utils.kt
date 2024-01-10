package com.yusuf.cryptocurrencytrading.utils

import android.content.Context
import android.view.View
import android.widget.Toast

class Utils{
    companion object{
        const val emailRegex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"
        const val nameRegex = "^[a-zA-ZğüşıöçĞÜŞİÖÇ]+$"

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

        fun nameControl(name:String,context: Context):Boolean{
            return if (name.matches(nameRegex.toRegex())){
                true;
            } else {
                Toast.makeText(context,"Name can not be empty!",Toast.LENGTH_LONG).show();
                false;
            }
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

