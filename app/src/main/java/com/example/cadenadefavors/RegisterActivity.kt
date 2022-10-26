package com.example.cadenadefavors

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.cadenadefavors.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase Auth
        auth = Firebase.auth


        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Log.d("TAG", "Cuack0")

        // Setup
        setup()
    }

    private fun setup() {
        title = "Registre"

        val userName = binding.editTextUserName.text.toString()

        binding.button2.setOnClickListener {
            if (binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(
                    binding.emailEditText.text.toString().trim(),
                    binding.passwordEditText.text.toString().trim()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("TAG", "Cuack2")
                        if (userName.length > 1) nameUser(userName)
                        showHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                    } else {
                        Log.d("TAG", "Cuack3")
                        Log.w("TAG", "createUserWithEmail:failure", it.exception)
                        showAlert()
                    }
                }
            }
        }

    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("L'operació ha fallat. Comprova el correu i la contrasenya (min 6 caràcters)")
        builder.setPositiveButton("Acceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email: String, provider: ProviderType) {
        val homeIntent = Intent(this, MainActivity::class.java)
        startActivity(homeIntent)
    }

    private fun nameUser(name: String) {
        val profileUpdates = userProfileChangeRequest {
            displayName = name
        }
    }
}