package com.example.pockie.presentation.ui.mainapp.friends.requests

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pockie.databinding.FriendItemBinding
import com.example.pockie.domain.model.User

class RequestsPageAdapter(private val onClick: (User) -> Unit): ListAdapter<User, RecyclerView.ViewHolder>(
    RequestPageDiffCallback()
) {

    private var isExpanded = false

    fun toggleExpand() {
        isExpanded = !isExpanded
        notifyDataSetChanged()
        Log.d("ADAPTER", "$itemCount")
    }

    inner class RequestPageViewHolder(private val binding: FriendItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                tvName.text = user.fullName
                tvUserName.text = user.userName
                ivAvatar.setImageResource(user.avatar)
            }
        }
    }

    override fun getItemCount(): Int {
        return if(isExpanded || currentList.size < 5) currentList.size else 5
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = FriendItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RequestPageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val user = getItem(position)
        (holder as RequestPageViewHolder).bind(user)
    }

    class RequestPageDiffCallback: DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

    }
}