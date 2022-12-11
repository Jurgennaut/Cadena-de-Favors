package com.example.cadenadefavors

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cadenadefavors.adapters.OfferRecyclerAdapter
import com.example.cadenadefavors.databinding.FragmentResultsBinding
import com.example.cadenadefavors.models.Offer

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [resultsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResultsFragment : Fragment() {

    private var filterCategory: String = "Menjar"

    private var _binding: FragmentResultsBinding? = null
    private val binding get() = _binding!!

    private val myAdapter: OfferRecyclerAdapter = OfferRecyclerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResultsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //recyclerView = binding.recyclerView
        //chooseLayout()
        setupRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun setupRecyclerView(){

        //Especifiquem que els fills del RV seran del mateix tamany i així optimitzem la seva creació
        binding.rvResults.setHasFixedSize(true)

        //indiquem que el RV es mostrarà en format llista
        binding.rvResults.layoutManager = GridLayoutManager(context,2);

        //generem el adapter
        myAdapter.OffersRecyclerAdapter(getResults(),requireContext())

        //assignem el adapter al RV
        binding.rvResults.adapter = myAdapter
    }
    private fun getResults() : MutableList<Offer>{

        val results: MutableList<Offer> = arrayListOf()
        results.add(Offer(
            "Entrepans!!",
            "restaurant_Amable",
            "Menjar",
            0,
            "Entrepans de tot tipus. Obrir xat per preguntar els preus",
            "https://okdiario.com/img/2022/02/08/receta-de-bocata-trufado.jpg"
        )
        )
        results.add(Offer(
            "PASSEJO GOSSOS",
            "carles_Cirera",
            "Animals de companyia",
            15,
            "M'ofereixo per passejar gossos. Tinc experiencia passejant diferents races de gos. 15/h",
            "https://cdn.royalcanin-weshare-online.io/_lbminYBBKJuub5q6J5F/v1/vf-a-spasso-col-cane-una-salute-un-benessere-442-800?h=675&w=1200&la=es-ES&fm=jpg"
        ))
        results.add(Offer(
            "CORTO EL CESPED",
            "albert_mateos",
            "Serveis domestics",
            5,
            "Corto el cesped de tu patio, 5 favos",
            "https://zulueta.com/wp-content/uploads/2017/03/cortar_cesped_fb.jpg"
        ))
        results.add(Offer(
            "Te hago un bocata",
            "jordi_oliver",
            "Menjar",
            5,
            "Te hago un bocata de lo que tenga por casa",
            "https://canalcocina.es/medias/_cache/zoom-72c7e28383e6bdc2f4399a9a61bf3cb7-920-518.jpg"
        ))

        return (results.filter { p -> p.offerCategory==filterCategory }).toMutableList();

    }

}