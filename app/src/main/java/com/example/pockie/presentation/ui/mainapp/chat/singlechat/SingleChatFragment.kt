package com.example.pockie.presentation.ui.mainapp.chat.singlechat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pockie.databinding.FragmentSingleChatBinding
import com.example.pockie.domain.model.Chat
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
        viewModel.getMessages(viewModel.getChatId(viewModel.getCurrentUserUid(), args.uid))
        setUpRecyclerView()
        getAccountName()
        observeMessages()
    }

    private fun setUpRecyclerView() {
        adapter = SingleChatAdapter()
        adapter.currentUid = viewModel.getCurrentUserUid()
        with(binding) {
            rvMessages.adapter = adapter
            rvMessages.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeMessages() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.messages.collectLatest { messages ->
                adapter.submitList(messages)
                binding.rvMessages.scrollToPosition(messages.size - 1)
            }
        }
    }

    private fun sendMessage() {
        val message = binding.etMessage.text.toString()
        if(message.isEmpty()) return
        viewModel.sendMessage(viewModel.getCurrentUserUid(), args.uid, message, Date())
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