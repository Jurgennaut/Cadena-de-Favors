package com.app.cadenadefavors

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.cadenadefavors.adapters.ChatAdapter
import com.app.cadenadefavors.databinding.ActivityListOfChatsBinding
import com.app.cadenadefavors.models.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

/**
 * CLASSE QUE CONTROLA LA VISTA ACTIVITY_LIST_OF_CHATS,
 * ON L'USUARI VISUALITZA UNA LLISTA DE TOTES LES SEVES CONVERSACIONS
 */
class ListOfChatsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListOfChatsBinding
    private lateinit var auth: FirebaseAuth

    private var user = ""
    private var db = Firebase.firestore

    /**
     * ONCREATE
     */
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



        var email: String?= intent.extras?.getString("email")
        if(email!=null){
            newChat(email)
        }


    }

    /**
     * INICIALITZA LA VISTA AMB UNA LLISTA AMB LES
     * CONVERSACIONS DE L'USUARI
     */
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

    /**
     * INTENT A ACTIVITY_CHAT, AMB LA INFORMACIÓ
     * DEL XAT SELECCIONAT
     */
    private fun chatSelected(chat: Chat){
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("chatId", chat.id)
        intent.putExtra("usuari", user)
        startActivity(intent)
    }

    /**
     * CREA UN NOU XAT
     */
    private fun newChat(email:String){
        val chatId = UUID.randomUUID().toString()
        val otherUser = email

        val users = listOf(user, otherUser!!)

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