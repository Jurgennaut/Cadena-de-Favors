package com.app.cadenadefavors


import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.app.cadenadefavors.adapters.OfferRecyclerAdapter
import com.app.cadenadefavors.databinding.FragmentResultsBinding
import com.app.cadenadefavors.models.Offer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [resultsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResultsFragment(userEmail:String) : Fragment() {

    private var email=userEmail
    private var _binding: FragmentResultsBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    val storage = Firebase.storage
    var storageRef = storage.reference

    private val myAdapter: OfferRecyclerAdapter = OfferRecyclerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResultsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //recyclerView = binding.recyclerView
        //chooseLayout()
        //val index = args!!.getInt("index", 0)
        Log.d("UWU", email)
        setupRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun setupRecyclerView() {

        //Especifiquem que els fills del RV seran del mateix tamany i així optimitzem la seva creació
        binding.rvResults.setHasFixedSize(true)

        //indiquem que el RV es mostrarà en format llista
        binding.rvResults.layoutManager = GridLayoutManager(context, 2);

        getUserFavors();
    }
    private fun getUserFavors(){
        var offers: MutableList<Offer> = arrayListOf()
        //generem el adapter

        db.collection("usuaris").document(email).collection("favors").get()
            .addOnSuccessListener { documents ->
                offers=documents.toObjects(Offer::class.java)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }.addOnCompleteListener {
                myAdapter.OffersRecyclerAdapter(offers,requireContext())

                //assignem el adapter al RV
                binding.rvResults.adapter = myAdapter
            }

    }

}