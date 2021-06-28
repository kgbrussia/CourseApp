package com.kgbrussia.library.recyclers

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kgbrussia.courseapp.library.R
import com.kgbrussia.java.ContactEntity

class ContactListAdapter(
    private val onItemClicked: (String) -> Unit
) : ListAdapter<ContactEntity, ContactViewHolder>(ContactListDiffUtilCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact_card, parent, false)
        return ContactViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ContactViewHolder(
    view: View,
    private val onItemClicked: (String) -> Unit
) : RecyclerView.ViewHolder(view) {
    private var contactImage: ImageView = view.findViewById(R.id.imageViewPhoto)
    private var contactName: TextView = view.findViewById(R.id.textViewName)
    private var contactNumber: TextView = view.findViewById(R.id.textViewPhoneNumber)

    fun bind(contact: ContactEntity) {
        contactName.text = contact.name
        contactNumber.text = contact.phone.ifEmpty { "unknown" }
        var photoUri: Uri? = null
        if (contact.photo != null) {
            photoUri = Uri.parse(contact.photo)
        }
        if (photoUri != null) {
            contactImage.setImageURI(photoUri)
        } else {
            contactImage.setImageResource(R.drawable.batman)
        }
        itemView.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onItemClicked(contact.id.toString())
            }
        }
    }
}

object ContactListDiffUtilCallback : DiffUtil.ItemCallback<ContactEntity>() {
    override fun areItemsTheSame(oldItem: ContactEntity, newItem: ContactEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ContactEntity, newItem: ContactEntity): Boolean {
        return if (oldItem.phone.isNotEmpty() && newItem.phone.isNotEmpty()) {
            oldItem.name == newItem.name &&
                oldItem.phone == newItem.phone &&
                oldItem.photo == newItem.photo
        } else {
            oldItem.name == newItem.name && oldItem.photo == newItem.photo
        }
    }
}