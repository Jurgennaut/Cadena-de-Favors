package com.example.cadenadefavors

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cadenadefavors.adapters.ChatAdapter
import com.example.cadenadefavors.databinding.ActivityListOfChatsBinding
import com.example.cadenadefavors.models.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class ListOfChatsActivity(email: String="") : AppCompatActivity() {

    private lateinit var binding: ActivityListOfChatsBinding
    private lateinit var auth: FirebaseAuth

    private var user = ""
    private var userEmail = email
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        binding = ActivityListOfChatsBinding.inflate(layoutInflater)

        //setContentView(R.layout.activity_list_of_chats)

        //intent.getStringExtra("usuari")?.let { user = it }

        user = auth.currentUser!!.email.toString()

        //Log.d("TAG", "Cuack2 ${auth.currentUser?.email}")

        if (user.isNotEmpty()){
            initViews()
        }

        val view = binding.root
        setContentView(view)

        binding.newChatText.setText(userEmail)

        binding.newChatButton.setOnClickListener {

            newChat()
        }

    }

    private fun initViews(){

        binding.listChatsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.listChatsRecyclerView.adapter =
            ChatAdapter { chat ->
                chatSelected(chat)
            }

        val userRef = db.collection("usuaris").document(auth.currentUser!!.email!!)

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
        intent.putExtra("usuari", user)
        startActivity(intent)
    }

    private fun newChat(){
        val chatId = UUID.randomUUID().toString()
        val otherUser = binding.newChatText.text.toString()

        val users = listOf(user, otherUser)

        val chat = Chat(
            id = chatId,
            name = "Chat amb $otherUser",
            users = users
        )

        db.collection("chats").document(chatId).set(chat)
        db.collection("usuaris").document(auth.currentUser!!.email!!).collection("chats").document(chatId).set(chat)
        db.collection("usuaris").document(otherUser).collection("chats").document(chatId).set(chat)

        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("chatId", chatId)
        intent.putExtra("usuari", auth.currentUser!!.email!!)
        startActivity(intent)
    }
}