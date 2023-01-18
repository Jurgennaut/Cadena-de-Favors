package com.app.cadenadefavors

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.app.cadenadefavors.databinding.FragmentProfileBinding
import com.app.cadenadefavors.profile.ProfileOpinionsFragment
import com.google.firebase.storage.FirebaseStorage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
/*private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"*/

private var _binding: FragmentProfileBinding? = null
private val binding get() = _binding!!
private var currentFragment: String?= "ResultsFragment"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters

    val args: ProfileFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.chatButton.setOnClickListener{
            Log.d("tag",args.pUser.Email)
            val chatIntent = Intent(context, ListOfChatsActivity::class.java)
            chatIntent.putExtra("email",args.pUser.Email);
            startActivity(chatIntent)
        }

        manageBarMenu()
        var user=args.pUser
        binding.userName.text=user.Username
        binding.userRating.rating=user.Puntuation
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child(user.Photo)
        imageRef.downloadUrl.addOnSuccessListener { url ->
            binding.userPhoto.load(url)
        }.addOnFailureListener {
            Log.w("ERROR", "Error downloading image", it)
        }
        var fr : Fragment? =null
        var requestedFragment: String?=null
        fr = ResultsFragment(args.pUser.Email)
        requestedFragment="ResultsFragment"
        currentFragment=requestedFragment

        var fm=parentFragmentManager
        var ft=fm.beginTransaction()

        ft.replace(R.id.fragmentContainerView5,fr)
        ft.commit()
    }

    private fun manageBarMenu(){
        binding.profileMenuBar.setOnItemSelectedListener{
            var fr : Fragment? =null
            var requestedFragment: String?=null

            when(it.itemId){
                R.id.menu_Offers -> {
                    fr = ResultsFragment(args.pUser.Email)
                    val bundle = Bundle()
                    bundle.putString("email", args.pUser.Email)
                    fr.setArguments(bundle)

                    requestedFragment="ResultsFragment"
                }R.id.menu_Opinions -> {
                    fr=ProfileOpinionsFragment(args.pUser.Email)
                    requestedFragment="ProfileOpinionsFragment"
                }R.id.menu_info -> {
                    fr=ProfileOpinionsFragment(args.pUser.Email)
                    requestedFragment="ProfileOpinionsFragment"
                }
            }
            if(currentFragment==requestedFragment) {
                false
            }else{
                currentFragment=requestedFragment

                var fm=parentFragmentManager
                var ft=fm.beginTransaction()

                ft.replace(R.id.fragmentContainerView5,fr!!)
                ft.commit()
                true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}