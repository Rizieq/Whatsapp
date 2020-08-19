package com.rizieq.whatsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rizieq.whatsapp.R
import com.rizieq.whatsapp.utils.Message

class ConversationAdapter(private val messages: ArrayList<Message>, val userId: String?) :
    RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder>() {
    companion object {
        val MESSAGE_CURRENT_USER = 1 // pesan dari user
        val MESSAGE_OTHER_USER = 2 // pesan dari partner chat user
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ConversationViewHolder {
        if (viewType == MESSAGE_CURRENT_USER) { // menghubungkan layout item
            return ConversationViewHolder( // ke currentUser sesuai dengan
                LayoutInflater.from(parent.context) // data yang didapat dari Message
                    .inflate(R.layout.item_current_user_message, parent, false)
            )
        } else {
            return ConversationViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_other_user_message, parent, false)
            )
        }
    }

    override fun getItemCount() = messages.size
    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        holder.bindItem(messages[position])
    }

    override fun getItemViewType(position: Int): Int {
        if (messages[position].sentBy.equals(userId)) { // menentukan posisi layout item sesuai
            return MESSAGE_CURRENT_USER // dengan data pengirim, jika data
        } else { // sentBy = userId layout item akan
            return MESSAGE_OTHER_USER // dipasang dengan current user
        } // jika sentBy != userId layout item yang
    } // akan dipasang adalah partner/other

    fun addMessage(message: Message) {
        messages.add(message)
        notifyDataSetChanged()
    }

    class ConversationViewHolder(val view: View) :
        RecyclerView.ViewHolder(view) {
        fun bindItem(message: Message) {
            view.findViewById<TextView>(R.id.txt_message).text = message.message
        }
    }
}
