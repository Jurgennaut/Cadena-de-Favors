package com.app.cadenadefavors

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import com.app.cadenadefavors.databinding.FragmentTransferBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TransferFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

/**
 * CLASSE QUE CONTROLA LA VISTA FRAGMENT_TRANSFER,
 * ON L'USUARI POT TRANSFERIR FAVÒLARS A ALTRES USUARIS
 */
class TransferFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentTransferBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

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
        _binding = FragmentTransferBinding.inflate(inflater, container, false)
        auth = Firebase.auth

        binding.transferActivityBtn.setOnClickListener{
            if(binding.transferQuantity.text.isDigitsOnly() && binding.transferActivityCorreuBeneficiari.text.isNotEmpty()){
                restFavos()
                sumFavos()
            }
        }

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_transfer, container, false)
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TransferActivity.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TransferFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    /**
     * SUMA LA QUANTITAT TRANSFERIDA AL COBRADOR
     */
    private fun sumFavos(){

// Document reference
        val storyRef = db.collection("patrimonis").document(binding.transferActivityCorreuBeneficiari.text.toString().trim())

// Update read count
        storyRef.update("favos", FieldValue.increment(binding.transferQuantity.text.toString().toLong()))
            .addOnSuccessListener {
                Toast.makeText(context, "La transferència s'ha realitzat correctament", Toast.LENGTH_LONG).show()
            }
    }

    /**
     * RESTA LA QUANTITAT TRANSFERIDA AL PAGADOR
     */
    private fun restFavos(){

// Document reference
        val storyRef = db.collection("patrimonis").document(auth.currentUser!!.email.toString())

// Update read count
        storyRef.update("favos", FieldValue.increment(-(binding.transferQuantity.text.toString().toLong())))
            .addOnSuccessListener {  }
    }

    private fun transferRegister(){
        // Document reference
        val storyRef = db.collection("transaccions").document(binding.transferActivityCorreuBeneficiari.text.toString().trim())

// Update read count
        storyRef.update("favos", FieldValue.increment(binding.transferQuantity.text.toString().toLong()))
            .addOnSuccessListener {
                Toast.makeText(context, "La transferència s'ha realitzat correctament", Toast.LENGTH_LONG).show()
            }
    }
}