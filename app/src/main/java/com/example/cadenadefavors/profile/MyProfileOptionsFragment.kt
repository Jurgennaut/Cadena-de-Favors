package com.example.cadenadefavors.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.cadenadefavors.databinding.FragmentMyProfileOptionsBinding

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
            val action = MyProfileOptionsFragmentDirections.actionMyProfileOptionsFragmentToMyProfileViewFragment()
            view?.findNavController()?.navigate(action)
        }
        binding.BtnMyFavors.setOnClickListener {
            val action = MyProfileOptionsFragmentDirections.actionMyProfileOptionsFragmentToMyFavorsFragment()
            view?.findNavController()?.navigate(action)
        }
    }

}