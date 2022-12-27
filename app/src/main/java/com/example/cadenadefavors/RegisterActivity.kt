package com.example.cadenadefavors

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.nfc.Tag
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.cadenadefavors.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val REQUEST_CODE=42
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    val db = Firebase.firestore

    private val TAG = "Registro"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase Auth
        auth = Firebase.auth


        binding = ActivityRegisterBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        // Setup
        setup()
    }

    override fun onActivityResult(requestCode:Int, resultCode: Int, data: Intent?){
        if (requestCode== REQUEST_CODE && resultCode==Activity.RESULT_OK){
            val takenImage=data?.extras?.get("data") as Bitmap
        }else{
          super.onActivityResult(requestCode, resultCode, data)
        }
    }
    private fun setup() {

        binding.btnCamera.setOnClickListener{
            Log.d("TAG", "Cuack")

            val takePictureIntent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if(takePictureIntent.resolveActivity(this.packageManager)!=null){
                startActivityForResult(takePictureIntent, REQUEST_CODE)
            }else{
                Toast.makeText(this, "No es pot obrir la camara.", Toast.LENGTH_SHORT).show();
            }

        }
        title = "Registre"

        val userName = binding.editTextUserName.text.toString()

        binding.button2.setOnClickListener {
            Log.d("TAG", "Cuack1")

            if (binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()
                && binding.CPEditText.text.isNotEmpty() && binding.phoneEditText.text.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(
                    binding.emailEditText.text.toString().trim(),
                    binding.passwordEditText.text.toString().trim()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (userName.length > 1) nameUser(userName)

                        insertUserToDB()

                        showHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                    } else {
                        Log.w("TAG", "createUserWithEmail:failure", it.exception)
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
        val homeIntent = Intent(this, MainActivity2::class.java)
        startActivity(homeIntent)
    }

    private fun nameUser(name: String) {
        val profileUpdates = userProfileChangeRequest {
            displayName = name
        }
    }

    private fun insertUserToDB(){
        Log.d("TAG", "Cuack2 ${auth.currentUser?.email}")

        val user = hashMapOf(
            "actiu" to true,
            "nom d'usuari" to binding.editTextUserName.text.toString(),
            "contrasenya" to binding.passwordEditText.text.toString(),
            "codi postal" to binding.CPEditText.text.toString(),
            "correu electronic" to binding.emailEditText.text.toString(),
            "telefon" to binding.phoneEditText.text.toString()
        )

// Add a new document with a generated ID
        db.collection("usuaris").document(auth.currentUser!!.uid)
            .set(user)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }


    }
}