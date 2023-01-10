package com.example.cadenadefavors.adapters

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.cadenadefavors.models.Offer
import com.example.cadenadefavors.databinding.ItemOfferListBinding
import coil.api.load
import com.example.cadenadefavors.MainFragmentDirections
import com.example.cadenadefavors.profile.MyFavorsFragmentDirections
import com.google.android.gms.common.api.internal.LifecycleCallback.getFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage


class OfferRecyclerAdapter: RecyclerView.Adapter<OfferRecyclerAdapter.ViewHolder>() {
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    val storage = Firebase.storage
    var storageRef = storage.reference
    private var offers: MutableList<Offer> = ArrayList()
    lateinit var context: Context
    private var isEditable: Boolean = false

    //constructor de la classe on es passa la font de dades i el context sobre el que es mostrarà
    fun OffersRecyclerAdapter(offersList:MutableList<Offer>, contxt: Context, editable: Boolean = false){
        this.offers = offersList
        this.context = contxt
        this.isEditable = editable
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
                binding.txtOfferTitle.text = this.Title
               // binding.txtDescription.text = this.offerDescription
                //binding.txtOwner.text = "@"+this.offerOwner
                binding.txtPrice.text= "Preu: "+ (this.Price).toString()+" favos."
                //binding.txtOfferCategory.text="Categoria: "+this.offerCategory

                 //Monstrar la imatge des de Storage de Firebase
                 val storageRef = FirebaseStorage.getInstance().reference
                 val imageRef = storageRef.child(this.Image)
                 imageRef.downloadUrl.addOnSuccessListener { url ->

                     binding.imgOffer.load(url)
                     if(!isEditable){
                         offers.get(position).Image=url.toString()
                     }


                 }.addOnFailureListener {
                     Log.w(TAG, "Error deleting document", it)
                 }

                if(isEditable){
                    binding.BtnBorrar.visibility= View.VISIBLE;
                    binding.BtnEditar.visibility= View.VISIBLE;

                    binding.BtnBorrar.setOnClickListener{
                        db.collection("favors (cataleg)").document(this.documentId.toString())
                            .delete()
                            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
                            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
                        offers.remove(offers.get(position))
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position,offers.size);
                    }

                    binding.BtnEditar.setOnClickListener{view ->
                        val action = MyFavorsFragmentDirections.actionMyFavorsFragmentToAddOfferFragment(offers.get(position))
                        view.findNavController()?.navigate(action)
                    }
                }
            }
        }
        val item = offers.get(position)
        holder.bind(item)

        //estamblim un listener
        holder.itemView.setOnClickListener { view ->
            val selectedOffer=offers.get(position);
            val action = MainFragmentDirections.actionMainFragmentToOfferFragment(selectedOffer)
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