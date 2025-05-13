package com.example.pockie.presentation.ui.mainapp.chat.chatlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pockie.R
import com.example.pockie.databinding.FragmentChatListBinding
import com.example.pockie.domain.model.Account
import com.example.pockie.presentation.ui.mainapp.chat.ChatListItem
import com.example.pockie.presentation.utils.networkstate.NetworkState
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
            viewModel.usersState.collectLatest { state ->
                when(state) {
                    is NetworkState.Init -> {}
                    is NetworkState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.rvChats.visibility = View.GONE
                    }
                    is NetworkState.Success<*> -> {
                        binding.progressBar.visibility = View.GONE
                        binding.rvChats.visibility = View.VISIBLE
                        val chatListItems = state.data as? List<ChatListItem> ?: emptyList()
                        adapter.submitList(chatListItems)
                    }
                    is NetworkState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}