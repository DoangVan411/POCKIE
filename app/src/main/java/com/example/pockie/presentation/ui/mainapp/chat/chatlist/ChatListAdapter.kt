package com.example.pockie.presentation.ui.mainapp.chat.chatlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pockie.R
import com.example.pockie.databinding.ChatItemBinding
import com.example.pockie.domain.model.Account

class ChatListAdapter(private val onClick: (Account) -> Unit): ListAdapter<Account, RecyclerView.ViewHolder>(
    ChatListDiffUtilCallback()
) {

    inner class ChatListViewHolder(private val binding: ChatItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(account: Account) {
            with(binding) {
                ivAvatar.setImageResource(R.drawable.setting)
                tvName.text = account.fullName
                tvLatestMes.text = "No"
                tvTime.text = "19h"
                itemView.setOnClickListener {
                    onClick(account)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val user = getItem(position)
        (holder as ChatListViewHolder).bind(user)
    }

    class ChatListDiffUtilCallback: DiffUtil.ItemCallback<Account>() {
        override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean {
            return oldItem == newItem
        }
    }
}