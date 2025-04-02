package com.example.pockie.presentation.ui.mainapp.chat.singlechat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pockie.databinding.ReceivedMessageItemBinding
import com.example.pockie.databinding.SentMessageItemBinding
import com.example.pockie.domain.model.Message

class SingleChatAdapter(): ListAdapter<Message, RecyclerView.ViewHolder>(MessageDiffUtilCallback()) {

    inner class SentMessageViewHolder(private val binding: SentMessageItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            with(binding) {
                tvMessage.text = message.content
                itemView.setOnClickListener {
                    binding.tvTime.visibility = if (binding.tvTime.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                }
            }
        }
    }

    inner class ReceivedMessageViewHolder(private val binding: ReceivedMessageItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            with(binding) {
                tvMessage.text = message.content
                itemView.setOnClickListener {
                    binding.tvTime.visibility = if (binding.tvTime.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                }
            }
        }
    }

    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }

    override fun getItemViewType(position: Int): Int {
        val message = getItem(position)
        if (message.type == "sent") {
            return VIEW_TYPE_SENT
        } else {
            return VIEW_TYPE_RECEIVED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == VIEW_TYPE_SENT) {
            val binding = SentMessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            SentMessageViewHolder(binding)
        } else {
            val binding = ReceivedMessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ReceivedMessageViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position)
        if (holder is SentMessageViewHolder) {
            holder.bind(message)
        }
        else if (holder is ReceivedMessageViewHolder) {
            holder.bind(message)
        }
    }

    class MessageDiffUtilCallback: DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }

    }
}