package com.example.pockie.presentation.ui.mainapp.friends.requests

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pockie.R
import com.example.pockie.databinding.RequestItemBinding
import com.example.pockie.domain.model.Account
import com.example.pockie.domain.model.FriendStatus
import com.example.pockie.domain.model.SearchResult

class FindUserAdapter(private val onButtonClick: (Account) -> Unit, private val onItemClick: (Account) -> Unit): ListAdapter<SearchResult, RecyclerView.ViewHolder>(
    SearchResultDiffCallback()
) {

    private var isExpanded = false

    inner class RequestPageViewHolder(private val binding: RequestItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(searchResult: SearchResult) {
            with(binding) {
                tvName.text = searchResult.account.fullName
                tvUserName.text = searchResult.account.email
                when (searchResult.status) {
                    FriendStatus.FRIEND -> {
                        ivAddFriend.setImageResource(R.drawable.friend)
                        ivAddFriend.isEnabled = false
                    }

                    FriendStatus.REQUEST_SENT -> {
                        ivAddFriend.setImageResource(R.drawable.arrow_sent)
                        ivAddFriend.isEnabled = false
                    }

                    FriendStatus.REQUEST_RECEIVED -> {
                        ivAddFriend.setImageResource(R.drawable.accept)
                        ivAddFriend.isEnabled = false
                    }

                    FriendStatus.NONE -> {
                        ivAddFriend.setImageResource(R.drawable.add_friend)
                        ivAddFriend.isEnabled = true
                        ivAddFriend.setOnClickListener {
                            onButtonClick(searchResult.account)
                            if(ivAddFriend.tag == "not_sent") {
                                ivAddFriend.tag = "sent"
                                ivAddFriend.setImageResource(R.drawable.arrow_sent)
                            } else {
                                ivAddFriend.tag = "not_sent"
                                ivAddFriend.setImageResource(R.drawable.add_friend)
                            }
                        }
                    }
                }
                itemView.setOnClickListener {
                    onItemClick(searchResult.account)
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

class RequestPageDiffCallback: DiffUtil.ItemCallback<Account>() {
    override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean {
        return oldItem == newItem
    }

}

class SearchResultDiffCallback: DiffUtil.ItemCallback<SearchResult>() {
    override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
        return oldItem == newItem
    }

}