package com.app.cadenadefavors

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.cadenadefavors.models.Message
import com.app.cadenadefavors.adapters.MessageAdapter
import com.app.cadenadefavors.databinding.ActivityChatBinding
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * CLASSE QUE CONTROLA LA VISTA ACTIVITY_CHAT,
 * ON L'USUARI VISUALITZA UNA LLISTA AMB TOTS
 * ELS MISSATGES DE LA CONVERSACIÓ, ORDENATS
 * CRONOLÒGICAMENT I ON POT ENVIAR MISSATGES A
 * ALTRES USUARIS
 */
class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    private var chatId = ""
    private var user = ""

    private var db = Firebase.firestore

    /**
     * ONCREATE
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        val intent = intent
        val message = intent.getStringExtra("message")
        if(!message.isNullOrEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("Notification")
                .setMessage(message)
                .setPositiveButton("Ok") { dialog, which -> }.show()
        }

        //setContentView(R.layout.activity_chat)

        intent.getStringExtra("chatId")?.let { chatId = it }
        intent.getStringExtra("usuari")?.let { user = it }

        if(chatId.isNotEmpty() && user.isNotEmpty()) {
            initViews()
        }
    }

    /**
     * INICIALITZA LA VISTA AMB UNA LLISTA AMB TOTS ELS MISSATGES DE LA
     * CONVERSACIÓ, ORDENATS CRONOLÒGICAMENT
     */
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

    /**
     * ENVIA UN MISSATGE
     */
    private fun sendMessage(){
        val message = Message(
            message = binding.messageTextField.text.toString(),
            from = user
        )

        db.collection("chats").document(chatId).collection("messages").document().set(message)



        binding.messageTextField.setText("")


    }
}