package com.example.cadenadefavors

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.cadenadefavors.databinding.FragmentAddofferBinding
import com.example.cadenadefavors.databinding.FragmentMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddOfferFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddOfferFragment : Fragment(), AdapterView.OnItemSelectedListener {
    val categoriesList = ArrayList<String>()
    var spinnerCategories:Spinner? = null
    var textView_categories:TextView? = null

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentAddofferBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    private val TAG = "addOffer"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        textView_categories = binding.textViewCategoria
        spinnerCategories = binding.menuCategories
        categoriesList.add("Menjar")
        categoriesList.add("Automoció")
        categoriesList.add("Informàtica")
        categoriesList.add("Jardineria")

        spinnerCategories!!.onItemSelectedListener

        val aa = context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, categoriesList) }
        if (aa != null) {
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerCategories!!.setAdapter(aa)

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
        val view = binding.root
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_addoffer, container, false)
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
            "categoria" to "",
            "descripcio" to binding.editTextOfferDescription.text.toString(),
            "preu" to binding.editTextOfferPrice.text.toString(),
        )

// Add a new document with a generated ID
        db.collection("usuaris").document(auth.currentUser!!.uid).collection("favors").document()
            .set(offer)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }


    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        textView_categories!!.text = categoriesList[position]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}