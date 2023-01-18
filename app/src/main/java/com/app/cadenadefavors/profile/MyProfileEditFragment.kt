package com.app.cadenadefavors.profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import coil.api.load
import com.app.cadenadefavors.databinding.FragmentMyProfileEditBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyProfileEditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyProfileEditFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var imgJpg:ByteArray?=null
    private lateinit var bitmapfrombytes: Bitmap
    private var _binding: FragmentMyProfileEditBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private var hasPhoto=true
    val db = Firebase.firestore

    private var storage = FirebaseStorage.getInstance()
    private val TAG = "Registro"

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // Handle the returned Uri
        if(uri!=null){
            val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
            val nubitmap = redimensionarImagen( bitmap!!, 200f, 200f)
            val bos = ByteArrayOutputStream(1000)

            if (nubitmap != null) {
                nubitmap.compress(Bitmap.CompressFormat.PNG, 50, bos)

                imgJpg = bos.toByteArray() //podemos guardarla, etc

                bitmapfrombytes = BitmapFactory.decodeByteArray(imgJpg, 0, imgJpg!!.size, null)
                binding.btnCamera2.setImageBitmap(bitmapfrombytes)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentMyProfileEditBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        db.collection("usuaris").document(auth.currentUser!!.email.toString()).get()
            .addOnSuccessListener {user ->
                binding.editTextUserName2.setText(user["Username"].toString())
                binding.CPEditText2.setText(user["Postalcode"].toString())
                binding.phoneEditText2.setText(user["Phone"].toString())

                Log.d("FOTO",user["Photo"].toString())
                if(user["Photo"]==null){
                    hasPhoto=false
                }else{
                    val storageRef = FirebaseStorage.getInstance().reference
                    val imageRef = storageRef.child(user["Photo"].toString())
                    imageRef.downloadUrl.addOnSuccessListener { url ->
                        binding.btnCamera2.load(url)
                    }.addOnFailureListener {
                        Log.w("ERROR", "Error downloading image", it)
                    }
                }
            }
        setup()
    }

    private fun setup(){
        binding.btnCamera2.setOnClickListener{
            getContent.launch("image/*")
        }

        binding.btnSave22.setOnClickListener {
            var userDb= db.collection("usuaris").document(auth.currentUser!!.email.toString())
            userDb.update("Username",binding.editTextUserName2.text.toString(),
                "Postalcode",binding.CPEditText2.text.toString(),
                            "Phone",binding.phoneEditText2.text.toString()).addOnSuccessListener {
                                Ok()
            }
            if(imgJpg!=null){

                var photoPath="userImages/${auth.uid.toString()}/${auth.currentUser!!.email}"
                val sRef: StorageReference =
                    storage.reference.child(photoPath)
                sRef.putBytes(imgJpg!!)
                if(!hasPhoto){
                    userDb.update("Photo",photoPath)
                }
            }
        }
    }
    private fun Ok(){
        Snackbar.make(
            binding.root,
            "Registro guardado",
            BaseTransientBottomBar.LENGTH_LONG
        ).setBackgroundTint(Color.parseColor("#79ab3c")).show()
    }

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