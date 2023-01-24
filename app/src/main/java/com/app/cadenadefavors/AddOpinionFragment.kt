package com.app.cadenadefavors

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.app.cadenadefavors.databinding.FragmentAddOpinionBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddOpinionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
private var _binding: FragmentAddOpinionBinding? = null
private val binding get() = _binding!!
private lateinit var auth: FirebaseAuth

val db = Firebase.firestore

/**
 * CLASSE QUE CONTROLA LA VISTA FRAGMENT_ADD_OPINION,
 * ON L'USUARI POT PUNTUAR I COMENTAR EL SERVEI REBUT
 * PER PART D'UN ALTRE USUARI
 */
class AddOpinionFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val args: AddOpinionFragmentArgs by navArgs()

    /**
     * ONCREATE
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    /**
     * ONCREATEVIEW
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddOpinionBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root
    }

    /**
     * ONVIEWCREATED
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child(args.pUser.Photo)

        imageRef.downloadUrl.addOnSuccessListener { url ->
            binding.imageViewOpinionerPhoto.load(url)
        }.addOnFailureListener {
            Log.w("ERROR", "Error downloading image", it)
        }
        binding.textUsername.text=args.pUser.Username
        binding.textOfferTitle.text=args.pOffer.Title

        binding.scoreUserBtn.setOnClickListener(){
            val score=binding.ratingBar.rating.toString()
            //Toast.makeText(context,score , Toast.LENGTH_SHORT).show()
            Log.d("TAG", "ANTES")
            insertOpinionToDb();
        }
    }

    /**
     * INSEREIX UNA OPINIÓ A LA BASE DE DADES
     */
    private fun insertOpinionToDb(){

        var puntuation=binding.ratingBar.rating.toString().toFloat()
        val opinion = hashMapOf(
            "Description" to binding.opinionDescriptionTxt.text.toString(),
            "Puntuation" to puntuation,
            "Owner" to auth.currentUser!!.email,
            "Favor" to args.pOffer.documentId
        )

        var dbuser=db.collection("usuaris").document(args.pUser.Email);

        dbuser.collection("valoracions").document()
                .set(opinion)
                .addOnSuccessListener {
                    Ok()
                }
                .addOnFailureListener {
                    Toast.makeText(context,"ERROR" , Toast.LENGTH_SHORT).show()
                }
        dbuser.update(
            "TotalPuntuation", FieldValue.increment(puntuation.toDouble()),
            "TimesPuntuated",FieldValue.increment(1)).addOnSuccessListener {

                dbuser.get()
                    .addOnSuccessListener { user->

                        dbuser.update("Puntuation", (user["TotalPuntuation"].toString().toDouble()/user["TimesPuntuated"].toString().toDouble()))
                    }
        }


    }

    /**
     * INFORMA A L'USUARI QUE TOT HA ANAT BÉ
     */
    private fun Ok(){
        Snackbar.make(
            binding.root,
            "Registro guardado",
            BaseTransientBottomBar.LENGTH_LONG
        ).setBackgroundTint(Color.parseColor("#79ab3c")).show()
    }


}