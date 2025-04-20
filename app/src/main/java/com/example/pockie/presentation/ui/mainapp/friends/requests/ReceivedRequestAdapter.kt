package com.example.pockie.presentation.ui.mainapp.friends.requests


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pockie.R
import com.example.pockie.databinding.RequestItemBinding
import com.example.pockie.domain.model.Account

class ReceivedRequestAdapter(private val onButtonClick: (Account) -> Unit, private val onItemClick: (Account) -> Unit): ListAdapter<Account, RecyclerView.ViewHolder>(
    RequestPageDiffCallback()
) {

    private var isExpanded = false

    fun toggleExpand() {
        isExpanded = !isExpanded
        notifyDataSetChanged()
    }

    inner class RequestPageViewHolder(private val binding: RequestItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(account: Account) {
            with(binding) {
                ivAddFriend.setImageResource(R.drawable.accept)
                tvName.text = account.fullName
                tvUserName.text = account.email
                ivAddFriend.setOnClickListener {
                    onButtonClick(account)
                    ivAddFriend.setImageResource(R.drawable.friend)
                }
                itemView.setOnClickListener {
                    onItemClick(account)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if(isExpanded || currentList.size < 5) currentList.size else 5
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
