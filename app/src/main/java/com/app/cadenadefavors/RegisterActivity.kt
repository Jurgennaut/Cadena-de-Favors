package com.app.cadenadefavors

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.app.cadenadefavors.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream

private const val REQUEST_CODE=42

/**
 * CLASSE QUE CONTROLA LA VISTA ACTIVITY_REGISTER,
 * ON L'USUARI ES POT DONAR D'ALTA A L'APP
 */
class RegisterActivity : AppCompatActivity() {

    private var imgJpg:ByteArray?=null
    private lateinit var bitmapfrombytes:Bitmap
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    val db = Firebase.firestore

    private var storage = FirebaseStorage.getInstance()
    private val TAG = "Registro"

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // Handle the returned Uri
        if(uri!=null){
            val bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri)
            val nubitmap = redimensionarImagen( bitmap!!, 200f, 200f)
            val bos = ByteArrayOutputStream(1000)

            if (nubitmap != null) {
                nubitmap.compress(Bitmap.CompressFormat.PNG, 50, bos)

                imgJpg = bos.toByteArray() //podemos guardarla, etc

                bitmapfrombytes = BitmapFactory.decodeByteArray(imgJpg, 0, imgJpg!!.size, null)
                binding.btnCamera.setImageBitmap(bitmapfrombytes)
            }
        }
    }

    /**
     * ONCREATE
     */
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

    /**
     * MANIPULA LA IMATGE INSERIDA PER L'USUARI
     */
    override fun onActivityResult(requestCode:Int, resultCode: Int, data: Intent?){
        if (requestCode== REQUEST_CODE && resultCode==Activity.RESULT_OK){
            val takenImage=data?.extras?.get("data") as Bitmap
        }else{
          super.onActivityResult(requestCode, resultCode, data)
        }
    }

    /**
     * CONFIGURA LA VISTA I ELS SEUS BOTONS
     */
    private fun setup() {
        title = getString(R.string.registre)

        binding.btnCamera.setOnClickListener{
            getContent.launch("image/*")
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
                        uploadImageToStorage(imgJpg!!)

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

    /**
     * MOSTRA UN MISSATGE D'ALERTA A L'USUARI
     */
    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("L'operació ha fallat. Comprova el correu i la contrasenya (min 6 caràcters)")
        builder.setPositiveButton("Acceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    /**
     * INTENT A FRAGMENT_MAIN
     */
    private fun showHome(email: String, provider: ProviderType) {
        val homeIntent = Intent(this, MainActivity2::class.java)
        startActivity(homeIntent)
    }

    /**
     * MOSTRA EL NOM DE L'USUARI QUE HA INICIAT SESSIÓ
     */
    private fun nameUser(name: String) {
        val profileUpdates = userProfileChangeRequest {
            displayName = name
        }
    }

    /**
     * INSEREIX UN USUARI A LA BASE DE DADES
     */
    private fun insertUserToDB(){
        Log.d("TAG", "Cuack2 ${auth.currentUser?.email}")

        val user = hashMapOf(
            "Active" to true,
            "Username" to binding.editTextUserName.text.toString(),
            "Password" to binding.passwordEditText.text.toString(),
            "Postalcode" to binding.CPEditText.text.toString(),
            "Email" to binding.emailEditText.text.toString(),
            "Phone" to binding.phoneEditText.text.toString(),
            "Photo" to "/userImages/${auth.uid}/${auth.currentUser!!.email}",
            "Puntuation" to 0
        )

        val heritage = hashMapOf(
            "favos" to 0
        )

// Add a new document with a generated ID
        db.collection("usuaris").document(auth.currentUser!!.email!!)
            .set(user)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

        db.collection("patrimonis").document(auth.currentUser!!.email!!)
            .set(heritage)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    /**
     * PUJA LA IMATGE PASSADA PER PARÀMETRE A L'STORAGE
     */
    private fun uploadImageToStorage(data:ByteArray){
        val sRef: StorageReference =
            storage.reference.child("userImages/${auth.uid.toString()}/${auth.currentUser!!.email}")
        sRef.putBytes(data)
        /*.addOnSuccessListener { task -> sRef.downloadUrl.addOnSuccessListener { uri ->
        urlFromStorage = uri
    }}*/

    }

    /**
     * REDIMENSIONA LA IMATGE PASSADA PER PARÀMETRE
     */
    fun redimensionarImagen(mBitmap: Bitmap, newWidth: Float, newHeigth: Float): Bitmap? {
        //Redimensionem
        val width = mBitmap.width
        val height = mBitmap.height
        var scaleWidth = newWidth / width
        var scaleHeight = newHeigth / height

        //ens quedem amb l'escalat més petit per que no es deformi la imatge
        if( scaleWidth > scaleHeight ) scaleWidth = scaleHeight
        else scaleHeight = scaleWidth

        // create a matrix for the manipulation
        val matrix = Matrix()
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight)
        // recreate the new Bitmap
        return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false)
    }
}