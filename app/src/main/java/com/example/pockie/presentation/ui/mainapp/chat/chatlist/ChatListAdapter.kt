package com.example.pockie.presentation.ui.mainapp.chat.chatlist

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pockie.R
import com.example.pockie.databinding.ChatItemBinding
import com.example.pockie.domain.model.Account
import com.example.pockie.presentation.ui.mainapp.chat.ChatListItem

class ChatListAdapter(private val onClick: (Account) -> Unit): ListAdapter<ChatListItem, RecyclerView.ViewHolder>(
    ChatListDiffUtilCallback()
) {

    inner class ChatListViewHolder(private val binding: ChatItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chatListItem: ChatListItem) {
            with(binding) {
                Log.d("Post", chatListItem.account.avtUrl)
                Glide.with(ivAvatar.context).load(chatListItem.account.avtUrl).into(ivAvatar)
                tvName.text = chatListItem.account.fullName
                tvLatestMes.text = chatListItem.lastMessage
                tvTime.text = chatListItem.lastMessageTimestamp
                itemView.setOnClickListener {
                    onClick(chatListItem.account)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chatListItem = getItem(position)
        (holder as ChatListViewHolder).bind(chatListItem)
    }

    class ChatListDiffUtilCallback: DiffUtil.ItemCallback<ChatListItem>() {
        override fun areItemsTheSame(oldItem: ChatListItem, newItem: ChatListItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ChatListItem, newItem: ChatListItem): Boolean {
            return oldItem == newItem
        }
    }
}