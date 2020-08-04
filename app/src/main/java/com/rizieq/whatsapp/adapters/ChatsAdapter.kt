package com.rizieq.whatsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rizieq.whatsapp.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_chats.*

class ChatsAdapter(val chats: ArrayList<String>) :
    RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder>() {
    private var chatClickListener: ChatClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ChatsViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_chats, parent, false
        )
    )

    override fun getItemCount() = chats.size
    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        holder.bindItem(chats[position], chatClickListener)
    }

    fun setOnItemClickListener(listener: ChatClickListener) {
        chatClickListener = listener
        notifyDataSetChanged()
    }

    fun updateChats(updatedChats: ArrayList<String>) {
        chats.clear()
        chats.addAll(updatedChats)
        notifyDataSetChanged()
    }

    class ChatsViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bindItem(chatId: String, listener: ChatClickListener?) {
// menghubungkan gambar dengan ImageView, jika terjadi error gambar diset ic_user populateImage(img_chats.context, "", img_chats, R.drawable.ic_user)
            txt_chats.text = chatId
        }
    }

    interface ChatClickListener {
        fun onChatClicked(
            name: String?, otherUserId: String?, chatsImageUrl: String?,
            chatsName: String?
        )
    }
}