package com.example.cadenadefavors

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
            if (binding.emailEditTextLogIn.text.isNotEmpty() && binding.passwordEditTextLogIn.text.isNotEmpty()) {
                auth.signInWithEmailAndPassword(
                    binding.emailEditTextLogIn.text.toString().trim(),
                    binding.passwordEditTextLogIn.text.toString().trim()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        //Log.d("TAG", "Cuack2")
                        showHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                    } else {
                        Log.w("TAG", "signInUserWithEmailAndPassword:failure", it.exception)
                        showAlert()
                    }
                }
            } else {
                Toast.makeText(
                    baseContext,
                    "Els camps correu i contrasenya no poden estar buits",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.registerLink.setOnClickListener {
            showRegister()
        }

        binding.passwordRecovery.setOnClickListener {
            showPasswdRecov()
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
        val homeIntent = Intent(this, MainActivity2::class.java)
        startActivity(homeIntent)
    }

    private fun showRegister() {
        val homeIntent = Intent(this, RegisterActivity::class.java)
        startActivity(homeIntent)
    }

    private fun showPasswdRecov() {
        val homeIntent = Intent(this, RecoverPasswordActivity::class.java)
        startActivity(homeIntent)
    }
}