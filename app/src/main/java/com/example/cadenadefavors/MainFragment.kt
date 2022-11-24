package com.example.cadenadefavors

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cadenadefavors.adapters.OfferRecyclerAdapter
import com.example.cadenadefavors.databinding.FragmentMainBinding
import com.example.cadenadefavors.models.Offer

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
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
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun setupRecyclerView(){
        binding.button6.setOnClickListener{
            showPopup(binding.button6)
        }

        //Especifiquem que els fills del RV seran del mateix tamany i així optimitzem la seva creació
        binding.rvOffers.setHasFixedSize(true)

        //indiquem que el RV es mostrarà en format llista
        binding.rvOffers.layoutManager = LinearLayoutManager(context)

        //generem el adapter
        myAdapter.OffersRecyclerAdapter(getOffers(),requireContext())

        //assignem el adapter al RV
        binding.rvOffers.adapter = myAdapter
    }
    private fun getOffers() : MutableList<Offer>{

        val offers: MutableList<Offer> = arrayListOf()
        offers.add(Offer(
            "Entrepans!!",
            "restaurant_Amable",
            "Menjar",
            0,
            "Entrepans de tot tipus. Obrir xat per preguntar els preus",
            "https://okdiario.com/img/2022/02/08/receta-de-bocata-trufado.jpg"
        )
        )
        offers.add(Offer(
            "PASSEJO GOSSOS",
            "carles_Cirera",
            "Animals de companyia",
            15,
            "M'ofereixo per passejar gossos. Tinc experiencia passejant diferents races de gos. 15/h",
            "https://cdn.royalcanin-weshare-online.io/_lbminYBBKJuub5q6J5F/v1/vf-a-spasso-col-cane-una-salute-un-benessere-442-800?h=675&w=1200&la=es-ES&fm=jpg"
        ))
        offers.add(Offer(
            "CORTO EL CESPED",
            "albert_mateos",
            "Serveis domestics",
            5,
            "Corto el cesped de tu patio, 5 favos",
            "https://zulueta.com/wp-content/uploads/2017/03/cortar_cesped_fb.jpg"
        ))
        offers.add(Offer(
            "Te hago un bocata",
            "jordi_oliver",
            "Menjar",
            5,
            "Te hago un bocata de lo que tenga por casa",
            "https://canalcocina.es/medias/_cache/zoom-72c7e28383e6bdc2f4399a9a61bf3cb7-920-518.jpg"
        ))

        return offers

    }
    private fun showPopup(v: View){
        val popup= PopupMenu(context,v)
        val inflater: MenuInflater =popup.menuInflater
        inflater.inflate(R.menu.menu_principal, popup.menu)
        popup.show()
    }
}