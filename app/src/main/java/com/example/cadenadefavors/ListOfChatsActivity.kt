package com.example.cadenadefavors

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cadenadefavors.adapters.ChatAdapter
import com.example.cadenadefavors.databinding.ActivityListOfChatsBinding
import com.example.cadenadefavors.models.Chat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class ListOfChatsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListOfChatsBinding

    private var user = ""

    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListOfChatsBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        //setContentView(R.layout.activity_list_of_chats)

        intent.getStringExtra("user")?.let { user = it }

        if (user.isNotEmpty()){
            initViews()
        }
    }

    private fun initViews(){
        binding.newChatButton.setOnClickListener { newChat() }

        binding.listChatsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.listChatsRecyclerView.adapter =
            ChatAdapter { chat ->
                chatSelected(chat)
            }

        val userRef = db.collection("users").document(user)

        userRef.collection("chats")
            .get()
            .addOnSuccessListener { chats ->
                val listChats = chats.toObjects(Chat::class.java)

                (binding.listChatsRecyclerView.adapter as ChatAdapter).setData(listChats)
            }

        userRef.collection("chats")
            .addSnapshotListener { chats, error ->
                if(error == null){
                    chats?.let {
                        val listChats = it.toObjects(Chat::class.java)

                        (binding.listChatsRecyclerView.adapter as ChatAdapter).setData(listChats)
                    }
                }
            }
    }

    private fun chatSelected(chat: Chat){
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("chatId", chat.id)
        intent.putExtra("user", user)
        startActivity(intent)
    }

    private fun newChat(){
        val chatId = UUID.randomUUID().toString()
        val otherUser = binding.newChatText.text.toString()
        val users = listOf(user, otherUser)

        val chat = Chat(
            id = chatId,
            name = "Chat con $otherUser",
            users = users
        )

        db.collection("chats").document(chatId).set(chat)
        db.collection("users").document(user).collection("chats").document(chatId).set(chat)
        db.collection("users").document(otherUser).collection("chats").document(chatId).set(chat)

        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("chatId", chatId)
        intent.putExtra("user", user)
        startActivity(intent)
    }
}