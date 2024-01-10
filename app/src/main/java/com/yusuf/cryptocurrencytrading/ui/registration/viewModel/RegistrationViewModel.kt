package com.yusuf.cryptocurrencytrading.ui.registration.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.yusuf.cryptocurrencytrading.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {

     val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
     val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    fun signUp(email: String, password: String, name: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        if (email.isNotEmpty() && password.isNotEmpty()) {

            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->
                    val user = hashMapOf(
                        "email" to email,
                        "username" to name,
                        "password" to password
                    )

                    firestore.collection("users")
                        .document(authResult.user?.uid ?: "")
                        .set(user)
                        .addOnSuccessListener {
                            onSuccess()
                        }
                        .addOnFailureListener {
                            onFailure(it.localizedMessage ?: "An unknown error occurred.")
                        }

                }.addOnFailureListener {
                    onFailure(it.localizedMessage ?: "An unknown error occurred.")
                }
        } else {
            onFailure("E-mail or Password can not be empty.")
        }
    }
}