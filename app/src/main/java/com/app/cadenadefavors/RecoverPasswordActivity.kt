package com.app.cadenadefavors

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class RecoverPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recoverpassword)

        title = getString(R.string.recupera_contrasenya)
    }
}