package com.example.pockie.presentation.ui.mainapp.chat.singlechat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pockie.databinding.FragmentSingleChatBinding
import com.example.pockie.domain.model.Chat
import com.example.pockie.presentation.utils.networkstate.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date

@AndroidEntryPoint
class SingleChatFragment(): Fragment() {

    private var _binding: FragmentSingleChatBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SingleChatViewModel by viewModels()
    private lateinit var adapter: SingleChatAdapter

    private val args: SingleChatFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSingleChatBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            btnSend.setOnClickListener {
                sendMessage()
            }
        }
        viewModel.getMessages(args.uid)
        setUpRecyclerView()
        getAccountName()
        collectMessages()

    }

    private fun setUpRecyclerView() {
        adapter = SingleChatAdapter()
        adapter.currentUid = viewModel.getCurrentUserUid()
        with(binding) {
            rvMessages.adapter = adapter
            rvMessages.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun collectMessages() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.messageState.collectLatest { state ->
                when (state) {
                    is NetworkState.Init -> { /* Hide all UI or do nothing */ }
                    is NetworkState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.rvMessages.visibility = View.GONE
                    }
                    is NetworkState.Success<*> -> {
                        binding.progressBar.visibility = View.GONE
                        binding.rvMessages.visibility = View.VISIBLE
                        val messages = state.data as? List<Chat> ?: emptyList()
                        Log.d("Messages", "${messages.size}")
                        adapter.submitList(messages) {
                            binding.rvMessages.postDelayed ({
                                if(messages.isNotEmpty()) {
                                    binding.rvMessages.post {
                                        binding.rvMessages.scrollToPosition(adapter.itemCount - 1)
                                    }
                                }
                            }, 100)
                        }

                    }
                    is NetworkState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun sendMessage() {
        val message = binding.etMessage.text.toString()
        if(message.isEmpty()) return
        viewModel.sendMessage(viewModel.getCurrentUserUid(), args.uid, message, Date(), requireActivity())
        binding.etMessage.text.clear()
    }



    private fun getAccountName() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getAccount(args.uid)
            viewModel.account.collectLatest { account ->
                binding.tvName.text = account.fullName
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}