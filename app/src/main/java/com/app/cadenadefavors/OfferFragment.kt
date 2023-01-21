package com.app.cadenadefavors

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.app.cadenadefavors.databinding.FragmentOfferBinding
import com.app.cadenadefavors.models.User
import com.google.firebase.storage.FirebaseStorage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OfferFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OfferFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentOfferBinding? = null
    private val binding get() = _binding!!

    val args: OfferFragmentArgs by navArgs()

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
        _binding = FragmentOfferBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val currentOffer=args.pOffer
        val storageRef = FirebaseStorage.getInstance().reference

        binding.currentOfferImage.load(currentOffer.Image);
        binding.currentOfferDescription.text=currentOffer.Description
        binding.currentOfferPreu.text=currentOffer.Price.toString()+" Favos"
        binding.currentOfferTitle.text=currentOffer.Title

        //email currentOffer.Owner
        db.collection("usuaris").document(currentOffer.Owner).get()
            .addOnSuccessListener { user ->

                var userObj=user.toObject(User::class.java)!!
                val imageRef = storageRef.child(user["Photo"].toString())

                imageRef.downloadUrl.addOnSuccessListener { url ->
                    binding.offerOwnerImage.load(url)
                }.addOnFailureListener {
                    Log.w("ERROR", "Error downloading image", it)
                }

                binding.offerOwnerUsername.text=user["Username"].toString()
                binding.userPuntuation.rating=user["Puntuation"].toString().toFloat()
                binding.offerOwnerImage.setOnClickListener{
                    val action = OfferFragmentDirections.actionOfferFragmentToProfileFragment(userObj)
                    view.findNavController()?.navigate(action)
                }


                binding.button6.setOnClickListener(){
                    val action = OfferFragmentDirections.actionOfferFragmentToAddOpinionFragment(userObj!!, currentOffer!!)
                    view.findNavController()?.navigate(action)
                }

            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OfferFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OfferFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}