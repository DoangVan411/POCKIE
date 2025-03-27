package com.example.pockie.presentation.ui.chat.chatlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pockie.databinding.ChatItemBinding
import com.example.pockie.domain.model.User

class ChatListAdapter(private val onClick: (User) -> Unit): ListAdapter<User, RecyclerView.ViewHolder>(ChatListDiffUtilCallback()) {

    inner class ChatListViewHolder(private val binding: ChatItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                ivAvatar.setImageResource(user.avatar)
                tvName.text = user.fullName
                tvLatestMes.text = user.latestMessage
                tvTime.text = user.time
                itemView.setOnClickListener {
                    onClick(user)
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

    class ChatListDiffUtilCallback: DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}