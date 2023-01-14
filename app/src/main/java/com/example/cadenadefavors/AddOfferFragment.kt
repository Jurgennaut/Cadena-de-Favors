package com.example.cadenadefavors

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
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
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.example.cadenadefavors.databinding.FragmentAddofferBinding
import com.example.cadenadefavors.models.Offer
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
    private var imgJpg:ByteArray?=null
    private lateinit var urlFromStorage:Uri

    private var oldOffer: Offer?=null

    private var editable:Boolean=false

    private val TAG = "addOffer"

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // Handle the returned Uri
        if(uri!=null){
            val bitmap = MediaStore.Images.Media.getBitmap(context?.getContentResolver(), uri)
            val nubitmap = redimensionarImagen( bitmap!!, 200f, 200f)
            val bos = ByteArrayOutputStream(1000)

            if (nubitmap != null) {
                nubitmap.compress(Bitmap.CompressFormat.PNG, 50, bos)

                imgJpg = bos.toByteArray() //podemos guardarla, etc

                bitmapfrombytes = BitmapFactory.decodeByteArray(imgJpg, 0, imgJpg!!.size, null)
                binding.imageViewFromDevice.setImageBitmap(bitmapfrombytes)
            }
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
            getContent.launch("image/*")
        }

        binding.BtnSave.setOnClickListener{
            var offerImage:String?=null
            if(imgJpg!=null){
                uploadImageToStorage(imgJpg!!)
                insertOfferToDB("/userImages/${auth.uid.toString()}/${imgJpg}", it)
            }else{
                if(editable){
                    insertOfferToDB(oldOffer!!.Image, it)
                }else{
                    Toast.makeText(context, "Debes añadir una imagen!", Toast.LENGTH_LONG).show()
                }
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if(arguments?.isEmpty!! || arguments==null){

        }else{
            editable=true

            binding.textView.text=getString(R.string.edit_favor)
            binding.BtnSave.text=getString(R.string.save_favor)

            val args: AddOfferFragmentArgs by navArgs()
            oldOffer=args.oldOffer

            binding.editTextTextOfferTitle.setText(oldOffer!!.Title)
            binding.editTextOfferPrice.setText(oldOffer!!.Price.toString())
            binding.editTextOfferDescription.setText(oldOffer!!.Description)

            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child(oldOffer!!.Image)
            imageRef.downloadUrl.addOnSuccessListener { url ->
                binding.imageViewFromDevice.load(url)
            }.addOnFailureListener {
                Log.w("ERROR", "Error downloading image", it)
            }

            // Seleccionar categoria
            var spinner=binding.menuCategories
            var length=binding.menuCategories.adapter.count

            for (i in 0 until length) {
                if (spinner.getItemAtPosition(i).toString() == oldOffer!!.Category) {
                    spinner.setSelection(i)
                    break
                }
            }
        }

    }




    private fun insertOfferToDB(offerImage:String, view:View){
        Log.d("TAG", "Cuack2 ${auth.currentUser?.email}")


        val offer = hashMapOf(
            "Title" to binding.editTextTextOfferTitle.text.toString(),
            "Category" to binding.menuCategories.selectedItem.toString(),
            "Description" to binding.editTextOfferDescription.text.toString(),
            "Price" to binding.editTextOfferPrice.text.toString().toInt(),
            "Image" to offerImage
        )

//        db.collection("usuaris").document(auth.currentUser!!.uid).collection("favors").document()
//            .set(offer)
//            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!")
//            }
//            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

        if(editable){
            db.collection("usuaris")
                .document(auth.currentUser!!.email.toString()).collection("favors")
                .document(oldOffer!!.documentId.toString())
                .set(offer)
                .addOnSuccessListener { Ok() }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

        }else{
            db.collection("usuaris")
                .document(auth.currentUser!!.email.toString()).collection("favors")
                .document()
                .set(offer)
                .addOnSuccessListener { Ok() }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        }

    }
    private fun Ok(){
        Snackbar.make(
            binding.root,
            "Registro guardado",
            BaseTransientBottomBar.LENGTH_LONG
        ).setBackgroundTint(Color.parseColor("#79ab3c")).show()
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
