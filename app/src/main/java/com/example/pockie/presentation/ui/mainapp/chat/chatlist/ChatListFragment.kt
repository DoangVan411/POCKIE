package com.example.pockie.presentation.ui.mainapp.chat.chatlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pockie.R
import com.example.pockie.databinding.FragmentChatListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatListFragment : Fragment() {
    private var _binding: FragmentChatListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChatListViewModel by viewModels()

    private lateinit var adapter: ChatListAdapter

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

        viewModel.getAccounts()

        setUpRecyclerView()
        observeAccounts()

    }

    private fun setUpRecyclerView () {
        adapter = ChatListAdapter(){account ->
            val action = ChatListFragmentDirections.actionChatListFragmentToSingleChatFragment(account.uid)
            findNavController().navigate(action)
        }
        with(binding) {
            rvChats.adapter = adapter
            rvChats.layoutManager = LinearLayoutManager(requireContext())

        }
    }

    private fun observeAccounts() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userList.collectLatest { userList ->
                adapter.submitList(userList)
                binding.rvChats.scrollToPosition(userList.size - 1)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}