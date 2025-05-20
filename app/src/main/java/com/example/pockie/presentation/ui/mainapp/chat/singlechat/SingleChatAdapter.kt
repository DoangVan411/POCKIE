package com.example.pockie.presentation.ui.mainapp.chat.singlechat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pockie.databinding.ReceivedMessageItemBinding
import com.example.pockie.databinding.SentMessageItemBinding
import com.example.pockie.domain.model.Chat
import com.example.pockie.domain.model.ChatItem
import com.example.pockie.presentation.utils.Utils.getTime

class SingleChatAdapter:
    ListAdapter<ChatItem, RecyclerView.ViewHolder>(MessageDiffUtilCallback()) {

    inner class SentMessageViewHolder(private val binding: SentMessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Chat, avt: String) {
            with(binding) {
                Glide.with(ivAvatar.context).load(avt).into(ivAvatar)
                tvMessage.text = chat.content
                tvTime.text = getTime(chat.createdAt)
                itemView.setOnClickListener {
                    binding.tvTime.visibility =
                        if (binding.tvTime.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                }

                if(!chat.imgUrl.isNullOrEmpty()){
                    tvMessage.visibility = View.INVISIBLE
                    Glide.with(img.context).load(chat.imgUrl).into(img)
                    tvMessageReply.text = chat.content
                    replyPost.visibility = View.VISIBLE
                }
            }
        }
    }

    inner class ReceivedMessageViewHolder(private val binding: ReceivedMessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Chat, avt: String) {
            with(binding) {
                Glide.with(ivAvatar.context).load(avt).into(ivAvatar)
                tvMessage.text = chat.content
                tvTime.text = getTime(chat.createdAt)
                itemView.setOnClickListener {
                    binding.tvTime.visibility =
                        if (binding.tvTime.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                }

                if(!chat.imgUrl.isNullOrEmpty()){
                    tvMessage.visibility = View.INVISIBLE
                    Glide.with(img.context).load(chat.imgUrl).into(img)
                    tvMessageReply.text = chat.content
                    replyPost.visibility = View.VISIBLE
                }
            }
        }
    }

    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }

    var currentUid: String = ""

    override fun getItemViewType(position: Int): Int {
        val message = getItem(position)
        return if (message.chat.senderId == currentUid) {
            VIEW_TYPE_SENT
        } else {
            VIEW_TYPE_RECEIVED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            val binding =
                SentMessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            SentMessageViewHolder(binding)
        } else {
            val binding = ReceivedMessageItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            ReceivedMessageViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position)
        if (holder is SentMessageViewHolder) {
            holder.bind(message.chat, message.senderAvt)
        } else if (holder is ReceivedMessageViewHolder) {
            holder.bind(message.chat, message.receiverAvt)
        }
    }

    class MessageDiffUtilCallback : DiffUtil.ItemCallback<ChatItem>() {
        override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
            return oldItem == newItem
        }

    }
}