package com.example.cadenadefavors

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity


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
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.resultats)

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

        return (results.filter { p -> p.Category==filterCategory }).toMutableList();

    }

}