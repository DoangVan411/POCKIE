package com.example.pockie.presentation.ui.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pockie.databinding.NotiItemBinding
import com.example.pockie.domain.model.Notification

class NotificationAdapter(private val onClick: (Notification) -> Unit): ListAdapter<Notification, RecyclerView.ViewHolder>(NotificationDiffUtilCallback()) {

    inner class NotificationViewHolder(private val binding: NotiItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(notification: Notification) {
            binding.apply {
                tvName.text = notification.name
                ivAvatar.setImageResource(notification.avatar)
                ivIcon.setImageResource(notification.icon)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = NotiItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val notification = getItem(position)
        (holder as NotificationViewHolder).bind(notification)
    }

    class NotificationDiffUtilCallback: DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem == newItem
        }
    }
}