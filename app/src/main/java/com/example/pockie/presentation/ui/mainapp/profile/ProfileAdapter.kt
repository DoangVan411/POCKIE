package com.example.pockie.presentation.ui.mainapp.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pockie.databinding.PostItemBinding
import com.example.pockie.domain.model.Post
import com.example.pockie.presentation.utils.Utils.getTime

class ProfileAdapter(private val onClick: (Post) -> Unit): ListAdapter<Post, RecyclerView.ViewHolder>(
    ProfileDiffUtilCallback()
) {

    inner class ProfileViewHolder(private val binding: PostItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            Glide.with(binding.iv).load(post.imageUrl).into(binding.iv)
            binding.tvDate.text = getTime(post.createAt)
//            binding.tvLikeCount.text = "${post.likeCount}"
            itemView.setOnClickListener {
                onClick(post)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post = getItem(position)
        (holder as ProfileViewHolder).bind(post)
    }

    class ProfileDiffUtilCallback: DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }

    }

}