package com.example.cadenadefavors

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.findFragment
import com.example.cadenadefavors.databinding.FragmentProfileBinding
import com.example.cadenadefavors.profile.ProfileOpinionsFragment

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
        manageBarMenu()
    }

    private fun manageBarMenu(){
        binding.profileMenuBar.setOnItemSelectedListener{
            var fr : Fragment? =null
            var requestedFragment: String?=null

            when(it.itemId){
                R.id.menu_Offers -> {
                    fr = ResultsFragment()
                    requestedFragment="ResultsFragment"
                }R.id.menu_Opinions -> {
                    fr=ProfileOpinionsFragment()
                    requestedFragment="ProfileOpinionsFragment"
                }R.id.menu_info -> {
                    fr=ProfileOpinionsFragment()
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