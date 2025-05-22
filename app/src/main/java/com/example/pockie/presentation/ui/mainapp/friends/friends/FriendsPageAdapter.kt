package com.example.pockie.presentation.ui.mainapp.friends.friends


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pockie.R
import com.example.pockie.databinding.RequestItemBinding
import com.example.pockie.domain.model.Account
import com.example.pockie.presentation.ui.mainapp.friends.requests.RequestPageDiffCallback

class FriendsPageAdapter(private val onButtonRemoveClick: (Account) -> Unit, private val onItemClick: (Account) -> Unit): ListAdapter<Account, RecyclerView.ViewHolder>(
    RequestPageDiffCallback()
) {

    inner class RequestPageViewHolder(private val binding: RequestItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(account: Account) {
            with(binding) {
                Glide.with(ivAvatar.context).load(account.avtUrl).into(ivAvatar)
                ivAddFriend.setImageResource(R.drawable.friend)
                cvRemove.visibility = View.VISIBLE
                tvName.text = account.fullName
                tvUserName.text = account.email
                ivRemove.setOnClickListener {
                    onButtonRemoveClick(account)
                }
                itemView.setOnClickListener {
                    onItemClick(account)
                }
                ivAddFriend.setOnClickListener {
                    onItemClick(account)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = RequestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RequestPageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val account = getItem(position)
        (holder as RequestPageViewHolder).bind(account)
    }
}
