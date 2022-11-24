package com.example.cadenadefavors

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cadenadefavors.adapters.OfferRecyclerAdapter
import com.example.cadenadefavors.adapters.OpinionRecyclerAdapter
import com.example.cadenadefavors.databinding.FragmentProfileBinding
import com.example.cadenadefavors.models.Offer
import com.example.cadenadefavors.models.Opinion
import androidx.navigation.findNavController as findNavController1

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
/*private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"*/

private var _binding: FragmentProfileBinding? = null
private val binding get() = _binding!!

private val myAdapter: OpinionRecyclerAdapter = OpinionRecyclerAdapter()

private lateinit var recyclerView: RecyclerView
private var isLinearLayoutManager = true

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        //recyclerView = binding.recyclerView
        //chooseLayout()
        setup()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_principal, menu)

/*        val layoutButton = menu.findItem(R.id.action_switch_layout)
        setIcon(layoutButton)*/
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        /*return when (item.itemId) {
            R.id.action_switch_layout -> {
                isLinearLayoutManager = !isLinearLayoutManager
                chooseLayout()
                setIcon(item)

                return true
            }

            else -> super.onOptionsItemSelected(item)
        }*/
        return true
    }

    /*private var param1: String? = null
    private var param2: String? = null

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
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }*/

    /*companion object {
        *
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }*/

    private fun getOpinions() : MutableList<Opinion>{
        val opinions: MutableList<Opinion> = arrayListOf()
        opinions.add(
            Opinion(
            "AlbertElLiante",
            "Estava trencat, jo no he estat",
            "Portàtil",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTXEfVUCNin7RsJNc3_pcL-4Mqk8STnqlGtuQ&usqp=CAU",
            R.drawable.puntuacion_max
        )
        )
        opinions.add(
            Opinion(
            "Gerardo V",
            "El gos no es va cansar gaire..",
            "Passejar el gos",
            "https://okdiario.com/img/2022/02/08/receta-de-bocata-trufado.jpg",
            R.drawable.puntuacion_media
        )
        )
        opinions.add(
            Opinion(
                "Jordi O",
                "Molt bo, tot perfecte",
                "Entrepà",
                "https://okdiario.com/img/2022/02/08/receta-de-bocata-trufado.jpg",
                R.drawable.puntuacion_quasi_max
            )
        )
        opinions.add(
            Opinion(
                "AlbertElLiante",
                "Estava trencat, jo no he estat",
                "Portàtil",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTXEfVUCNin7RsJNc3_pcL-4Mqk8STnqlGtuQ&usqp=CAU",
                R.drawable.puntuacion_max
            )
        )

        return opinions
    }

    private fun setupRecyclerView(){

        //Especifiquem que els fills del RV seran del mateix tamany i així optimitzem la seva creació
        binding.rvOpinions.setHasFixedSize(true)

        //indiquem que el RV es mostrarà en format llista
        binding.rvOpinions.layoutManager = LinearLayoutManager(context)

        //generem el adapter
        myAdapter.OpinionRecyclerAdapter(getOpinions()) //, getContext())

        //assignem el adapter al RV
        binding.rvOpinions.adapter = myAdapter
    }

    private fun setup() {
        binding.addOfferBtn.setOnClickListener() {
            val action = ProfileFragmentDirections.actionProfileFragmentToAddOfferFragment()
            view?.findNavController1()?.navigate(action)

        }
        binding.resultsBtn.setOnClickListener(){
            val action = ProfileFragmentDirections.actionProfileFragmentToResultsFragment()

            this.view?.findNavController1()?.navigate(action)

        }

        setupRecyclerView()
    }

    private fun chooseLayout() {
        /*when (isLinearLayoutManager) {
            true -> {
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = LetterAdapter()
            }
            false -> {
                recyclerView.layoutManager = GridLayoutManager(context, 4)
                recyclerView.adapter = LetterAdapter()
            }
        }*/
    }

    private fun setIcon(menuItem: MenuItem?) {
        /*if (menuItem == null)
            return

        menuItem.icon =
            if (isLinearLayoutManager)
                ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_grid_layout)
            else ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_linear_layout)*/
    }
}