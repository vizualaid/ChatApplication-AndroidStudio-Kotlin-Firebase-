package com.example.chatapp

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context: Context,val messageList: ArrayList<Message>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val Item_Receive=1;
    val Iten_Sent=2;


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        if(viewType==1)
        {
        //inflate receive
            val view: View= LayoutInflater.from(context).inflate(R.layout.receive,parent,false)
            return ReceiveViewHolder(view)
        }
        else{
                //sent
            val view: View= LayoutInflater.from(context).inflate(R.layout.sent,parent,false)
            return SentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentMessage=messageList[position]
        if(holder.javaClass==SentViewHolder:: class.java){

            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text= currentMessage.message
        }
        else{
            //
            val viewHolder= holder as ReceiveViewHolder
            holder.receiveMessage.text= currentMessage.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage=messageList[position]
        return if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            Iten_Sent
        } else{
            Item_Receive
        }
    }

    override fun getItemCount(): Int {
      return messageList.size
    }

    class SentViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val sentMessage= itemView.findViewById<TextView>(R.id.sentMsg)
    }
    class ReceiveViewHolder (itemView: View):RecyclerView.ViewHolder(itemView){
        val receiveMessage= itemView.findViewById<TextView>(R.id.receiveMsg)
    }

}