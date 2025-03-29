package com.example.pockie.presentation.ui.friends.friends

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pockie.R
import com.example.pockie.databinding.FragmentFriendsBinding
import com.example.pockie.databinding.FragmentFriendsPageBinding
import com.example.pockie.domain.model.User

class FriendsPage : Fragment() {

    private var _binding: FragmentFriendsPageBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendsPageBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val adapter = FriendsPageAdapter(){}
        binding.rvFriends.adapter = adapter
        binding.rvFriends.layoutManager = LinearLayoutManager(requireContext())
        val list = listOf(
            User("vandoan", "Vân Đoàn", R.drawable.setting, "Hello", "1"),
            User("vandoan", "Vân Đoàn", R.drawable.setting, "Hello", "1"),
            User("vandoan", "Vân Đoàn", R.drawable.setting, "Hello", "1"),
        )
        Log.d("FriendsPage", "List size: ${list.size}")
        adapter.submitList(list)
        binding.tvFriendsCount.text = "Friends: ${adapter.currentList.size}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}