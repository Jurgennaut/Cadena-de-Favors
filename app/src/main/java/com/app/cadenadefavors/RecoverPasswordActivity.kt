package com.app.cadenadefavors

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/**
 * CLASSE QUE CONTROLA LA VISTA ACTIVITY_RECOVERPASSWORD,
 * ON L'USUARI POT RECUPERAR LA SEVA CONTRASENYA
 */
class RecoverPasswordActivity : AppCompatActivity() {

    /**
     * ONCREATE
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recoverpassword)

        title = getString(R.string.recupera_contrasenya)
    }
}