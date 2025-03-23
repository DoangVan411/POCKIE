package com.example.pockie.presentation.ui.authentication.addcontacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pockie.databinding.ItemContactBinding
import com.example.pockie.domain.model.Contact

class ContactsAdapter : ListAdapter<Contact, RecyclerView.ViewHolder>(ContactDiffUtilCallback()){

    inner class ContactViewHolder(private val binding: ItemContactBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(contact: Contact){
            binding.name.text = contact.name
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val contact = getItem(position)
        (holder as ContactViewHolder).bind(contact)
    }

    class ContactDiffUtilCallback: DiffUtil.ItemCallback<Contact>(){
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }

    }
}