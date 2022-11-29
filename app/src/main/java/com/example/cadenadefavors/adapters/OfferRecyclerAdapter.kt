package com.example.cadenadefavors.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.cadenadefavors.models.Offer
import com.example.cadenadefavors.databinding.ItemOfferListBinding
import coil.api.load
import com.example.cadenadefavors.MainFragmentDirections


class OfferRecyclerAdapter: RecyclerView.Adapter<OfferRecyclerAdapter.ViewHolder>() {
    var offers: MutableList<Offer> = ArrayList()
    lateinit var context: Context

    //constructor de la classe on es passa la font de dades i el context sobre el que es mostrarà
    fun OffersRecyclerAdapter(offersList:MutableList<Offer>, contxt: Context){
        this.offers = offersList
        this.context = contxt
    }

    //és l'encarregat de retornar el ViewHolder ja configurat
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            ItemOfferListBinding.inflate(
                layoutInflater, parent, false
            )
        )
    }

    //Aquest mètode s'encarrega de passar els objectes, un a un al ViewHolder personalitzat
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder){
            with(offers.get(position)){
                binding.txtOfferTitle.text = this.offerTitle
               // binding.txtDescription.text = this.offerDescription
                //binding.txtOwner.text = "@"+this.offerOwner
                binding.txtPrice.text= "Preu: "+ (this.offerPrice).toString()+" favos."
                binding.imgOffer.load(this.offerImage)
                //binding.txtOfferCategory.text="Categoria: "+this.offerCategory
                /*
                 //Monstrar la imatge des de Storage de Firebase
                 val storageRef = FirebaseStorage.getInstance().reference
                 val imageRef = storageRef.child("rv/${this.animalName}")
                 imageRef.downloadUrl.addOnSuccessListener { url ->
                     binding.imgAnimal.load(url)
                 }.addOnFailureListener {
                     //mostrar error
                 } */
            }
        }
        val item = offers.get(position)
        holder.bind(item)

        //estamblim un listener
        holder.itemView.setOnClickListener { view ->
            val selectedOffer=offers.get(position);

            Toast.makeText(context, selectedOffer.offerOwner,Toast.LENGTH_LONG).show()

            val action = MainFragmentDirections.actionMainFragmentToOfferFragment(selectedOffer.offerImage!!)
            view.findNavController()?.navigate(action)
        }
    }


    override fun getItemCount(): Int {
        return offers.size
    }




    class ViewHolder(val binding: ItemOfferListBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(offer: Offer) {

        }

    }


}