package com.example.cadenadefavors.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.cadenadefavors.MainFragmentDirections
import com.example.cadenadefavors.databinding.FragmentMainBinding
import com.example.cadenadefavors.databinding.ItemOpinionListBinding
import com.example.cadenadefavors.models.Opinion

import androidx.navigation.findNavController as findNavController1

class OpinionRecyclerAdapter: RecyclerView.Adapter<OpinionRecyclerAdapter.ViewHolder>() {
    var opinions: MutableList<Opinion> = ArrayList()
    lateinit var context: Context

    //constructor de la classe on es passa la font de dades i el context sobre el que es mostrarà
    fun OpinionRecyclerAdapter(opinionsList:MutableList<Opinion>){ //}, contxt: Context){
        this.opinions = opinionsList
        //this.context = contxt
    }

    //és l'encarregat de retornar el ViewHolder ja configurat
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            ItemOpinionListBinding.inflate(
                layoutInflater, parent, false
            )
        )
    }

    //Aquest mètode s'encarrega de passar els objectes, un a un al ViewHolder personalitzat
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder){
            with(opinions[position]){
                binding.textOpinionDescription.text = this.opinionDescription
                binding.textOpinionOwner.text = "@"+this.opinionOwner
                binding.textOpinionerFavorReceived.text = "Va demanar: \n"+this.opinionerFavorReceived
                binding.imageViewOpinionerPhoto.load(this.opinionerPhoto)
                binding.imageViewOpinionerStarsGiven.setImageResource(this.stars)
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
        val item = opinions[position]
        holder.bind(item)

        //estamblim un listener
        holder.itemView.setOnClickListener { view ->
            Toast.makeText(view.context, "ENTRAMOS A MENU LOGOUT",Toast.LENGTH_SHORT).show()
            Log.d("TAG","message: "+view.context)
        }
    }


    override fun getItemCount(): Int {
        Log.d("opinionProfile", "getCount ${opinions.size}")
        return opinions.size
    }




    class ViewHolder(val binding: ItemOpinionListBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(opinion: Opinion) {

        }

    }


}