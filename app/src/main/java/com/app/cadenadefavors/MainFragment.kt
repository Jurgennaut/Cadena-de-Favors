package com.app.cadenadefavors

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.app.cadenadefavors.adapters.OfferRecyclerAdapter
import com.app.cadenadefavors.databinding.FragmentMainBinding
import com.app.cadenadefavors.models.Offer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

/**
 * CLASSE QUE CONTROLA LA VISTA FRAGMENT_MAIN,
 * LA VISTA PRINCIPAL DE L'APP
 */
class MainFragment : Fragment() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    val storage = Firebase.storage
    var storageRef = storage.reference

    private val TAG = "cuackeando"

    private val myAdapter: OfferRecyclerAdapter = OfferRecyclerAdapter()

    /**
     * ONCREATE
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * ONCREATEVIEW
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        val view = binding.root
        return view
    }

    /**
     * ONVIEWCREATED
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.inici)

        setupRecyclerView()
        setupMenu()

        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                activity?.finishAffinity()
                return@setOnKeyListener true
            }
            false
        }
    }

    /**
     * CONFIGURA EL MENÚ PRINCIPAL
     */
    private fun setupMenu(){
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_principal, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_logout -> {
                        auth.signOut()
                        showLogIn()
                        true
                    }R.id.menu_myprofile -> {
                        val chatIntent = Intent(context, ListOfChatsActivity::class.java)
                        startActivity(chatIntent)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    /**
     * ONDESTROYVIEW
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * CONFIGURA EL RECYCLERVIEW
     */
    private fun setupRecyclerView(){

        //Especifiquem que els fills del RV seran del mateix tamany i així optimitzem la seva creació
        binding.rvOffers.setHasFixedSize(true)

        //indiquem que el RV es mostrarà en format llista
        binding.rvOffers.layoutManager = GridLayoutManager(context,2);

        //generem el adapter

        getOffers()
    }

    /**
     * RECUPERA OFERTES DE LA BD
     */
    private fun getOffers(){
        var offers: MutableList<Offer> = arrayListOf()

        db.collection("favors").get()
            .addOnSuccessListener { documents ->
                offers=documents.toObjects(Offer::class.java)
            }.addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }.addOnCompleteListener {
                myAdapter.OffersRecyclerAdapter(offers,requireContext())
                binding.rvOffers.adapter = myAdapter
            }

    }

    /**
     * INTENT A ACTIVITY_LOGIN
     */
    private fun showLogIn() {
        val LogInIntent = Intent(context, LoginActivity::class.java)
        LogInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(LogInIntent)

    }
}
