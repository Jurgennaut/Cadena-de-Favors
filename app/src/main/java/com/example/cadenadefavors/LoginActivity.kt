package com.example.cadenadefavors

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.cadenadefavors.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase Auth
        auth = Firebase.auth

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setup()
    }

    private fun setup() {
        title = "Autenticació"

        binding.login.setOnClickListener {
            Log.d("TAG", "Cuack")
            if (binding.emailEditTextLogIn.text.isNotEmpty() && binding.passwordEditTextLogIn.text.isNotEmpty()) {
                auth.signInWithEmailAndPassword(
                    binding.emailEditTextLogIn.text.toString(),
                    binding.passwordEditTextLogIn.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("TAG", "Cuack2")
                        showHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                    } else {
                        Log.d("TAG", "Cuack3")
                        Log.w("TAG", "signInUserWithEmailAndPassword:failure", it.exception)
                        showAlert()
                    }
                }
            }
        }

    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("S'ha produït un error autenticant l'usuari")
        builder.setPositiveButton("Acceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email: String, provider: ProviderType) {
        val homeIntent = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }
}