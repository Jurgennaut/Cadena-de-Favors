package com.app.cadenadefavors

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.app.cadenadefavors.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * CLASSE QUE CONTROLA LA VISTA ACTIVITY_LOGIN,
 * ON L'USUARI POT INICIAR SESSIÓ
 */
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    /**
     * ONCREATE
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(2000)
        installSplashScreen()
        // Initialize Firebase Auth
        auth = Firebase.auth
        var currentUser = FirebaseAuth.getInstance().currentUser
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setup()

    }

    /**
     * ES CRIDA QUAN L'USUARI FA UN CALLBACK
     */
    override fun onBackPressed() {
        finishAffinity()
    }

    /**
     * CONFIGURA LA VISTA I ELS BOTONS D'AQUESTA
     */
    private fun setup() {
        title = getString(R.string.autenticacio)

        binding.login.setOnClickListener {


            if (binding.emailEditTextLogIn.text.isNotEmpty() && binding.passwordEditTextLogIn.text.isNotEmpty()) {
                binding.login.isEnabled = false
                auth.signInWithEmailAndPassword(
                    binding.emailEditTextLogIn.text.toString().trim(),
                    binding.passwordEditTextLogIn.text.toString().trim()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        //Log.d("TAG", "Cuack2")
                        showHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                    } else {
                        binding.login.isEnabled = true
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

    /**
     * MOSTRA UN MISSATGE D'ALERTA A L'USUARI
     */
    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("S'ha produït un error autenticant l'usuari")
        builder.setPositiveButton("Acceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    /**
     * INTENT A ACTIVITY_MAIN2 (FRAGMENT MAIN COM A HOST)
     */
    private fun showHome(email: String, provider: ProviderType) {
        val homeIntent = Intent(this, MainActivity2::class.java)
        startActivity(homeIntent)
    }

    /**
     * INTENT A ACTIVITY_REGISTER
     */
    private fun showRegister() {
        val homeIntent = Intent(this, RegisterActivity::class.java)
        startActivity(homeIntent)
    }

    /**
     * INTENT A ACTIVITY_RECOVERPASSWORD
     */
    private fun showPasswdRecov() {
        val homeIntent = Intent(this, RecoverPasswordActivity::class.java)
        startActivity(homeIntent)
    }
}