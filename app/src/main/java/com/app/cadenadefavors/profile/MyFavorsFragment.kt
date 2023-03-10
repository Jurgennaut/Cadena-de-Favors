package com.app.cadenadefavors.profile

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.app.cadenadefavors.adapters.OfferRecyclerAdapter
import com.app.cadenadefavors.databinding.FragmentMyFavorsBinding
import com.app.cadenadefavors.models.Offer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyFavorsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyFavorsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentMyFavorsBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    val storage = Firebase.storage
    var storageRef = storage.reference

    private val myAdapter: OfferRecyclerAdapter = OfferRecyclerAdapter()
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
        _binding= FragmentMyFavorsBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
    }
    private fun setupRecyclerView(){

        //Especifiquem que els fills del RV seran del mateix tamany i aix?? optimitzem la seva creaci??
        binding.rvMyOffers.setHasFixedSize(true)

        //indiquem que el RV es mostrar?? en format llista
        binding.rvMyOffers.layoutManager = GridLayoutManager(context,2);

        //generem el adapter

        getOffers()

    }
    private fun getOffers(){
        var offers: MutableList<Offer> = arrayListOf()
        db.collection("usuaris").document(auth.currentUser!!.email.toString()).collection("favors").get()
            .addOnSuccessListener { documents ->
                offers=documents.toObjects(Offer::class.java)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }.addOnCompleteListener {
                myAdapter.OffersRecyclerAdapter(offers,requireContext(),true)
                binding.rvMyOffers.adapter = myAdapter
            }
    }

}