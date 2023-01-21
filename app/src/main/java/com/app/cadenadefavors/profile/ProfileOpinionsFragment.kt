package com.app.cadenadefavors.profile

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.cadenadefavors.adapters.OpinionRecyclerAdapter
import com.app.cadenadefavors.databinding.FragmentProfileOpinionsBinding
import com.app.cadenadefavors.models.Opinion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


private var _binding: FragmentProfileOpinionsBinding? = null
private val binding get() = _binding!!

private val myAdapter: OpinionRecyclerAdapter = OpinionRecyclerAdapter()

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileOpinionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileOpinionsFragment(userEmail: String) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var email=userEmail
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    val storage = Firebase.storage
    var storageRef = storage.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileOpinionsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
    }

    private fun setupRecyclerView(){

        //Especifiquem que els fills del RV seran del mateix tamany i així optimitzem la seva creació
        binding.rvOpinions.setHasFixedSize(true)

        //indiquem que el RV es mostrarà en format llista
        binding.rvOpinions.layoutManager = LinearLayoutManager(context)

        //generem el adapter
        getOpinions()
    }

    private fun getOpinions(){
        var opinions: MutableList<Opinion> = arrayListOf()
        db.collection("usuaris").document(email).collection("valoracions").get()
            .addOnSuccessListener { documents ->
                opinions=documents.toObjects(Opinion::class.java)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }.addOnCompleteListener {
                myAdapter.OpinionRecyclerAdapter(opinions) //, getContext())
                binding.rvOpinions.adapter = myAdapter
            }
    }

}