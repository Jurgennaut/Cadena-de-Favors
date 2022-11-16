package com.example.cadenadefavors

import android.content.Intent
import android.os.Bundle
import android.view.MenuInflater
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cadenadefavors.models.Offer
import com.example.cadenadefavors.databinding.ActivityMainBinding
import com.example.cadenadefavors.adapters.OfferRecyclerAdapter
enum class ProviderType {
    BASIC
}

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val myAdapter: OfferRecyclerAdapter = OfferRecyclerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(2000)
        installSplashScreen()
        binding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
    }

    private fun setupRecyclerView(){
        binding.button6.setOnClickListener{
            showPopup(binding.button6)
        }

        binding.provisional.setOnClickListener{
            showHome2()
        }

        binding.provisional2.setOnClickListener{
            showRegister()
        }

        //Especifiquem que els fills del RV seran del mateix tamany i així optimitzem la seva creació
        binding.rvOffers.setHasFixedSize(true)

        //indiquem que el RV es mostrarà en format llista
        binding.rvOffers.layoutManager = LinearLayoutManager(this)

        //generem el adapter
        myAdapter.OffersRecyclerAdapter(getOffers(),this)

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

        return offers
    }

    private fun showPopup(v: View){
        val popup=PopupMenu(this,v)
        val inflater:MenuInflater=popup.menuInflater
        inflater.inflate(R.menu.menu_principal, popup.menu)
        popup.show()
    }

    //Provisional
    private fun showHome2() {
        val homeIntent = Intent(this, MainActivity2::class.java)
        startActivity(homeIntent)
    }

    private fun showRegister() {
        val homeIntent = Intent(this, RegisterActivity::class.java)
        startActivity(homeIntent)
    }
}