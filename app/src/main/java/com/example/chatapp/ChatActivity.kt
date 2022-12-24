package com.example.chatapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>


    var receiverRoom: String?=null
    var senderRoom: String?=null
    private lateinit var mDbRef: DatabaseReference

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val name = intent.getStringExtra("name")
        val receiverUid=intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        mDbRef= FirebaseDatabase.getInstance().getReference()
        senderRoom= receiverUid + senderUid
        receiverRoom= senderUid + receiverUid

//        supportActionBar?.title = receiverUid
        this.supportActionBar?.title= name
            //.toString()

        chatRecyclerView=findViewById(R.id.charRecycleView)
        messageBox=findViewById(R.id.messageBox)
        sendButton=findViewById(R.id.sendButton)
        messageList=ArrayList()
        messageAdapter = MessageAdapter(this, messageList)

        chatRecyclerView.layoutManager= LinearLayoutManager(this)
        chatRecyclerView.adapter= messageAdapter
        //logic to add message to recyclerview
        mDbRef.child("chats").child(senderRoom!!).child("message")
            .addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()

                    for(postSnapshot in snapshot.children)
                    {
                        val message= postSnapshot.getValue(Message::class.java)
                      messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        // adding message to database
        sendButton.setOnClickListener{
        val message=messageBox.text.toString()
        val messageObject= Message(message,senderUid)
            mDbRef.child("chats").child(senderRoom!!).child("message").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDbRef.child("chats").child(receiverRoom!!).child("message").push()
                        .setValue(messageObject)

                }
            messageBox.setText("")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
//        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout)
        {    mAuth.signOut()
            val intent= Intent(this@ChatActivity,LogIn::class.java)
            finish()
            startActivity(intent)
            return true
        }
        return true
//        return super.onOptionsItemSelected(item)
    }
}