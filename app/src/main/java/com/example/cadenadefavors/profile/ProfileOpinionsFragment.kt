package com.example.cadenadefavors.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cadenadefavors.R
import com.example.cadenadefavors.models.Opinion
import com.example.cadenadefavors.adapters.OpinionRecyclerAdapter
import com.example.cadenadefavors.databinding.FragmentProfileBinding
import com.example.cadenadefavors.databinding.FragmentProfileOpinionsBinding


private var _binding: FragmentProfileOpinionsBinding? = null
private val binding get() = _binding!!

private val myAdapter: OpinionRecyclerAdapter = OpinionRecyclerAdapter()

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileOpinionsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileOpinionsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        myAdapter.OpinionRecyclerAdapter(getOpinions()) //, getContext())

        //assignem el adapter al RV
        binding.rvOpinions.adapter = myAdapter
    }

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

}