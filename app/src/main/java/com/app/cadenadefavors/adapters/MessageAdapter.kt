package com.app.cadenadefavors.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.cadenadefavors.models.Message
import com.app.cadenadefavors.databinding.ItemMessageBinding

class MessageAdapter(private val user: String): RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    private var messages: List<Message> = emptyList()

    fun setData(list: List<Message>){
        messages = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            ItemMessageBinding.inflate(
                layoutInflater, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder){
            val message = messages[position]

            if(user == message.from){
                binding.myMessageLayout.visibility = View.VISIBLE
                binding.otherMessageLayout.visibility = View.GONE

                binding.myMessageTextView.text = message.message
            } else {
                binding.myMessageLayout.visibility = View.GONE
                binding.otherMessageLayout.visibility = View.VISIBLE

                binding.othersMessageTextView.text = message.message
            }
        }


    }

    override fun getItemCount(): Int {
        return messages.size
    }

    class ViewHolder(val binding: ItemMessageBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {

        }

    }

    //class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}