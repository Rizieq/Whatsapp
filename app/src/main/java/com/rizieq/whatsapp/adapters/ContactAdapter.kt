package com.rizieq.whatsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rizieq.whatsapp.R
import com.rizieq.whatsapp.listeners.ContactsClickListener
import com.rizieq.whatsapp.utils.Contact
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_contacts.view.*

class ContactsAdapter(val contacts: ArrayList<Contact>) :
    RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {
    private var clickListener: ContactsClickListener? = null
    // menghubungkan LayoutItem dengan activity untuk menempatkan kontak ke dalam bentuk List
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ContactsViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_contacts,
            parent, false
        )
    )

    override fun getItemCount() = contacts.size // menghitung jumlah kontak yang didapat
    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.bindItem(contacts[position], clickListener)
    }

    class ContactsViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        // memasangkan data pada view dalam layout_item
        fun bindItem(contact: Contact, listener: ContactsClickListener?) {
            itemView.txt_contact_name.text = contact.name
            itemView.txt_contact_number.text = contact.phone
            itemView.setOnClickListener {
                // memberikan aksi ketika item kontak diklik
                listener?.onContactClicked(contact.name, contact.phone)
            }
        }
    }

    fun setOnItemClickListener(listener: ContactsClickListener) {
        clickListener = listener
        notifyDataSetChanged()
    }
}