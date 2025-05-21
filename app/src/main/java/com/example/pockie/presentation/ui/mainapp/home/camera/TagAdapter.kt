package com.example.pockie.presentation.ui.mainapp.home.camera

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pockie.R
import com.example.pockie.databinding.FragmentPostItemBinding
import com.example.pockie.databinding.TagItemBinding
import com.example.pockie.domain.model.PostItem
import com.example.pockie.domain.model.Tag
import com.example.pockie.presentation.utils.Utils
import com.google.firebase.auth.FirebaseAuth

class TagAdapter(private val onClick: (Tag) -> Unit) :
    ListAdapter<Tag, RecyclerView.ViewHolder>(TagDiffUtilCallback()) {
    private var selectedPosition = 1

    inner class TagViewHolder(private val binding: TagItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tag: Tag) {
            binding.name.text = tag.name
            binding.icon.setBackgroundResource(tag.icon)

            if (tag.id == selectedPosition) {
                binding.tagLayout.setBackgroundResource(R.drawable.tag_selected)
            } else {
                binding.tagLayout.setBackgroundResource(R.drawable.background_field)
            }

            itemView.setOnClickListener {
                selectedPosition = tag.id
                onClick(tag)
                notifyDataSetChanged()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val binding =
            TagItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TagViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentPostItem = getItem(position)
        (holder as TagViewHolder).bind(currentPostItem)
    }

    class TagDiffUtilCallback : DiffUtil.ItemCallback<Tag>() {
        override fun areItemsTheSame(oldItem: Tag, newItem: Tag): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Tag, newItem: Tag): Boolean {
            return oldItem == newItem
        }

    }
}