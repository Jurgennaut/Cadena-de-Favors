package com.example.cadenadefavors

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cadenadefavors.adapters.OfferRecyclerAdapter
import com.example.cadenadefavors.databinding.FragmentMainBinding
import com.example.cadenadefavors.models.Offer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    val storage = Firebase.storage
    var storageRef = storage.reference

    private val TAG = "cuackeando"

    private val myAdapter: OfferRecyclerAdapter = OfferRecyclerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.chatButton.setOnClickListener{
            val chatIntent = Intent(context, ListOfChatsActivity::class.java)
            startActivity(chatIntent)
        }

        setupRecyclerView()
        setupMenu()
    }

    private fun setupMenu(){
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_principal, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_logout -> {
                        Toast.makeText(context, "ENTRAMOS A MENU LOGOUT",Toast.LENGTH_LONG).show()
                        true
                    }R.id.menu_myprofile -> {
                        Toast.makeText(context, "ENTRAMOS A MENU MYPROFILE",Toast.LENGTH_LONG).show()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun setupRecyclerView(){

        //Especifiquem que els fills del RV seran del mateix tamany i així optimitzem la seva creació
        binding.rvOffers.setHasFixedSize(true)

        //indiquem que el RV es mostrarà en format llista
        binding.rvOffers.layoutManager = GridLayoutManager(context,2);

        //generem el adapter

        getOffers()
    }
    private fun getOffers(){
        val offers: MutableList<Offer> = arrayListOf()
        db.collection("favors (cataleg)").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    offers.add(Offer(
                        document.data["Title"].toString(),
                        document.data["Owner"].toString(),
                        document.data["Category"].toString(),
                        document.data["Price"].toString().toInt(),
                        document.data["Description"].toString(),
                        document.data["Image"].toString(),
                        ))
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }.addOnCompleteListener {
                myAdapter.OffersRecyclerAdapter(offers,requireContext())
                binding.rvOffers.adapter = myAdapter

            }
    }

}