package com.example.cadenadefavors

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cadenadefavors.databinding.FragmentAddOpinionBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
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

class AddOpinionFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
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
        _binding = FragmentAddOpinionBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.valoracio)

        binding.scoreUserBtn.setOnClickListener(){
            val score=binding.ratingBar.rating.toString()
            //Toast.makeText(context,score , Toast.LENGTH_SHORT).show()
            Log.d("TAG", "ANTES")
            insertOpinionToDb();
        }
    }
    private fun insertOpinionToDb(){

        val opinion = hashMapOf(
            "descripcio" to binding.opinionDescriptionTxt.text.toString(),
            "puntuacio" to binding.ratingBar.rating.toString().toDouble()
        )
// Add a new document with a generated ID
        val sdf = SimpleDateFormat("yyyyMddHmmss")
        val currentDate = sdf.format(Date())

        db.collection("usuaris").document("9IcZw2xLcNp4csRwdnSE")
            .collection("valoracions").document(auth.uid!!+"_"+currentDate)
                .set(opinion)
                .addOnSuccessListener {
                    Toast.makeText(context,"OPINION GRABADA CORRECTAMENTE" , Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context,"ERROR" , Toast.LENGTH_SHORT).show()
                }
    }


}