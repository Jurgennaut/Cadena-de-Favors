package com.example.cadenadefavors.adapters

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.cadenadefavors.databinding.ItemOpinionListBinding
import com.example.cadenadefavors.models.Opinion
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

import androidx.navigation.findNavController as findNavController1

class OpinionRecyclerAdapter: RecyclerView.Adapter<OpinionRecyclerAdapter.ViewHolder>() {
    var opinions: MutableList<Opinion> = ArrayList()

    val db = Firebase.firestore
    val storage = Firebase.storage
    var storageRef = storage.reference

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
                binding.textOpinionDescription.text = this.Description

                db.collection("favors").document(this.Favor).get()
                    .addOnSuccessListener { favor ->
                        binding.textOpinionerFavorReceived.text = favor["Title"].toString()
                    }

                db.collection("usuaris").document(this.Owner).get()
                    .addOnSuccessListener { user ->
                        binding.textOpinionOwner.text = user["Username"].toString()

                        val imageRef = storageRef.child(user["Photo"].toString())
                        imageRef.downloadUrl.addOnSuccessListener { url ->
                            binding.imageViewOpinionerPhoto.load(url)
                        }.addOnFailureListener {
                            Log.w(ContentValues.TAG, "Error deleting document", it)
                        }
                    }
                binding.ratingBar2.rating=this.Puntuation

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
            Toast.makeText(view.context, "CLICK",Toast.LENGTH_SHORT).show()
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