package com.example.cadenadefavors

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import com.example.cadenadefavors.databinding.FragmentAddofferBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddOfferFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddOfferFragment : Fragment() {


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentAddofferBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    private lateinit var bitmapfrombytes:Bitmap
    private lateinit var imgJpg:ByteArray
    private lateinit var urlFromStorage:Uri

    private val TAG = "addOffer"

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // Handle the returned Uri
        val bitmap = MediaStore.Images.Media.getBitmap(context?.getContentResolver(), uri)
        val nubitmap = redimensionarImagen( bitmap!!, 200f, 200f)
        val bos = ByteArrayOutputStream(1000)

        if (nubitmap != null) {
            nubitmap.compress(Bitmap.CompressFormat.PNG, 50, bos)

            imgJpg = bos.toByteArray() //podemos guardarla, etc

            bitmapfrombytes = BitmapFactory.decodeByteArray(imgJpg, 0, imgJpg.size, null)
            binding.imageViewFromDevice.setImageBitmap(bitmapfrombytes)
        }

    }

    private var storage = FirebaseStorage.getInstance()

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
        _binding = FragmentAddofferBinding.inflate(inflater, container, false)
        auth = Firebase.auth

        binding.buttonAddImage23.setOnClickListener{
            Log.d("TAG", "hola23")
            getContent.launch("image/*")
        }

        binding.button3.setOnClickListener{
            Log.d("TAG", "hola3")
            uploadImageToStorage(imgJpg)
            insertOfferToDB()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddOfferFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun insertOfferToDB(){
        Log.d("TAG", "Cuack2 ${auth.currentUser?.email}")

        val offer = hashMapOf(
            "categoria" to binding.menuCategories.selectedItem.toString(),
            "descripcio" to binding.editTextOfferDescription.text.toString(),
            "preu" to binding.editTextOfferPrice.text.toString(),
            "imatge" to "/userImages/${auth.uid.toString()}/${imgJpg}"
        )

// Add a new document with a generated ID
        db.collection("usuaris").document(auth.currentUser!!.uid).collection("favors").document()
            .set(offer)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }


    }

    private fun uploadImageToStorage(data:ByteArray){
        val sRef: StorageReference =
            storage.reference.child("userImages/${auth.uid.toString()}/${data}")
        sRef.putBytes(data)
            /*.addOnSuccessListener { task -> sRef.downloadUrl.addOnSuccessListener { uri ->
            urlFromStorage = uri
        }}*/

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
