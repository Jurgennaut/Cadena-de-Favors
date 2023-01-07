package com.example.cadenadefavors.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cadenadefavors.databinding.ItemChatBinding
import com.example.cadenadefavors.models.Chat

class ChatAdapter(val chatClick: (Chat) -> Unit): RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    var chats: List<Chat> = emptyList()

    fun setData(list: List<Chat>){
        chats = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            ItemChatBinding.inflate(
                layoutInflater, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder){
            binding.chatNameText.text = chats[position].name
            binding.usersTextView.text = chats[position].users.toString()

            itemView.setOnClickListener {
                chatClick(chats[position])
            }
        }

    }

    override fun getItemCount(): Int {
        return chats.size
    }

    class ViewHolder(val binding: ItemChatBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(chat: Chat) {

        }

    }

   // class ChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}