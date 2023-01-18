package com.app.cadenadefavors.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.app.cadenadefavors.databinding.FragmentMyProfileOptionsBinding
import com.app.cadenadefavors.models.User
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
 * Use the [MyProfileOptionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyProfileOptionsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentMyProfileOptionsBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    val storage = Firebase.storage
    var storageRef = storage.reference
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
        _binding = FragmentMyProfileOptionsBinding.inflate(inflater, container, false)
        auth=Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupBindings()
    }

    private fun setupBindings(){
        binding.BtnEditMyProfile.setOnClickListener{
            val action = MyProfileOptionsFragmentDirections.actionMyProfileOptionsFragmentToMyProfileEditFragment()
            view?.findNavController()?.navigate(action)
        }
        binding.BtnSeeMyProfile.setOnClickListener {

            db.collection("usuaris").document(auth.currentUser!!.email.toString()).get()
                .addOnSuccessListener { user->
                    var currentUser=user.toObject(User::class.java)
                    val action = MyProfileOptionsFragmentDirections.actionMyProfileOptionsFragmentToProfileFragment(currentUser!!)
                    view?.findNavController()?.navigate(action)
                }
        }
        binding.BtnMyFavors.setOnClickListener {
            val action = MyProfileOptionsFragmentDirections.actionMyProfileOptionsFragmentToMyFavorsFragment()
            view?.findNavController()?.navigate(action)
        }
    }

}