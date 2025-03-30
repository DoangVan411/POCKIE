package com.example.pockie.presentation.ui.chat.singlechat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pockie.R
import com.example.pockie.databinding.FragmentSingleChatBinding
import com.example.pockie.domain.model.Message

class SingleChatFragment : Fragment() {

    private var _binding: FragmentSingleChatBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SingleChatViewModel by viewModels()

    private val sampleMesList = listOf(
        Message("sent", "Oidoioi"),
        Message("received", "Oidoioi"),
        Message("sent", "Oidoioi"),
        Message("received", "Oidoioi"),
        Message("sent", "Oidoioi"),
        Message("received", "Oidoioi"),
        Message("sent", "Oidoioi"),
    )

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
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val adapter = SingleChatAdapter()
        with(binding) {
            rvMessages.adapter = adapter
            rvMessages.layoutManager = LinearLayoutManager(requireContext())
        }
        adapter.submitList(sampleMesList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}