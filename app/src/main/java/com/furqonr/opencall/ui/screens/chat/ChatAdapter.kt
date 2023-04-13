package com.furqonr.opencall.ui.screens.chat

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.furqonr.opencall.R
import com.furqonr.opencall.models.ChatModel
import com.furqonr.opencall.models.User
import com.furqonr.opencall.ui.utils.DateConverter
import com.google.firebase.auth.FirebaseUser

interface OnLongPressListener {
    fun chat(chat: ChatModel)
}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var cardView: CardView
    private var message: TextView
    private var dateText: TextView
    private var parentLayout: LinearLayout

    init {
        parentLayout = itemView.findViewById(R.id.layout_parent)
        cardView = itemView.findViewById(R.id.bubble_chat)
        message = itemView.findViewById(R.id.message)
        dateText = itemView.findViewById(R.id.date_message)
    }

    fun bindSender(chat: ChatModel, listener: OnLongPressListener) {
        parentLayout.gravity = Gravity.END
        dateText.gravity = Gravity.END
        bind(chat, listener)
    }

    fun bindReceiver(chat: ChatModel, listener: OnLongPressListener) {
        parentLayout.gravity = Gravity.START
        dateText.gravity = Gravity.START
        bind(chat, listener)
    }

    private fun bind(chat: ChatModel, listener: OnLongPressListener) {
        message.text = chat.message
        dateText.text = DateConverter(chat.timestamp.toString()).convert()
        cardView.setOnLongClickListener {
            listener.chat(chat)
            return@setOnLongClickListener true
        }
    }
}

class ChatAdapter(
    private val messages: MutableList<ChatModel>,
    private val currentUser: FirebaseUser,
    private val listener: OnLongPressListener
) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.content_chat, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return messages.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val uid = messages[position].sender
        println(uid == currentUser.uid)
        if (uid == currentUser.uid) {
            holder.bindSender(messages[position], listener)
        } else {
            holder.bindReceiver(messages[position], listener)
        }


//
//        if (currentMessage == currentUser.uid) {
//            holder.bindSender(messages[position], listener)
//        } else {
//            holder.bindReceiver(messages[position], listener)
//        }
    }
}