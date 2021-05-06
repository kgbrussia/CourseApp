package com.kgbrussia.courseapp

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ContactListAdapter(private val itemClickListener: ItemClickListener?) :
    ListAdapter<Contact, ContactViewHolder>(DiffUtilCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact_card, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            if(adapterPosition != RecyclerView.NO_POSITION) {
                itemClickListener?.onItemClicked(getItem(adapterPosition).id.toString())
            }
        }
    }
}

class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private var contactImage: ImageView = view.findViewById(R.id.imageViewPhoto)
    private var contactName: TextView = view.findViewById(R.id.textViewName)
    private var contactNumber: TextView = view.findViewById(R.id.textViewPhoneNumber)

    fun bind(contact: Contact) {
        contactName.text = contact.name
        contactNumber.text = if (contact.phone.isNotEmpty()) contact.phone else ""
        val photoUri: Uri? = contact.photo
        if (photoUri != null) contactImage.setImageURI(photoUri)
        else contactImage.setImageResource(R.drawable.batman)
    }
}

object DiffUtilCallback : DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        val contentsTheSame: Boolean
        if(oldItem.phone.isNotEmpty() && newItem.phone.isNotEmpty()){
            contentsTheSame = oldItem.name == newItem.name &&
                    oldItem.phone == newItem.phone &&
                    oldItem.photo == newItem.photo
        }else{
            contentsTheSame = oldItem.name == newItem.name &&
                    oldItem.photo == newItem.photo
        }
        return contentsTheSame
    }
}

interface ItemClickListener {
    fun onItemClicked(id: String)
}