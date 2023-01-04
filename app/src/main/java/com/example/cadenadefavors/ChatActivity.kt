package com.example.cadenadefavors

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cadenadefavors.models.Message
import com.example.cadenadefavors.adapters.MessageAdapter
import com.example.cadenadefavors.databinding.ActivityChatBinding
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    private var chatId = ""
    private var user = ""

    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        //setContentView(R.layout.activity_chat)

        intent.getStringExtra("chatId")?.let { chatId = it }
        intent.getStringExtra("user")?.let { user = it }

        if(chatId.isNotEmpty() && user.isNotEmpty()) {
            initViews()
        }
    }

    private fun initViews(){
        binding.messagesRecylerView.layoutManager = LinearLayoutManager(this)
        binding.messagesRecylerView.adapter = MessageAdapter(user)

        binding.sendMessageButton.setOnClickListener { sendMessage() }

        val chatRef = db.collection("chats").document(chatId)

        chatRef.collection("messages").orderBy("dob", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { messages ->
                val listMessages = messages.toObjects(Message::class.java)
                (binding.messagesRecylerView.adapter as MessageAdapter).setData(listMessages)
            }

        chatRef.collection("messages").orderBy("dob", Query.Direction.ASCENDING)
            .addSnapshotListener { messages, error ->
                if(error == null){
                    messages?.let {
                        val listMessages = it.toObjects(Message::class.java)
                        (binding.messagesRecylerView.adapter as MessageAdapter).setData(listMessages)
                    }
                }
            }
    }

    private fun sendMessage(){
        val message = Message(
            message = binding.messageTextField.text.toString(),
            from = user
        )

        db.collection("chats").document(chatId).collection("messages").document().set(message)

        binding.messageTextField.setText("")


    }
}