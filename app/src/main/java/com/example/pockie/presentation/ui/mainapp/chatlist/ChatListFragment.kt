package com.example.pockie.presentation.ui.mainapp.chatlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pockie.R
import com.example.pockie.databinding.FragmentChatListBinding
import com.example.pockie.domain.model.User

class ChatListFragment : Fragment() {
    private var _binding: FragmentChatListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChatListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentChatListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }

        setUpRecyclerView()
    }

    private fun setUpRecyclerView () {
        val sampleList = listOf(
            User("vandoan", "Vân Đoàn", R.drawable.setting, "Hello", "19h"),
            User("vandoan", "Vân Đoàn", R.drawable.setting, "Hello", "19h"),
            User("vandoan", "Vân Đoàn", R.drawable.setting, "Hello", "19h"),
        )
        val adapter = ChatListAdapter(){}
        with(binding) {
            rvChats.adapter = adapter
            rvChats.layoutManager = LinearLayoutManager(requireContext())
            adapter.submitList(sampleList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}