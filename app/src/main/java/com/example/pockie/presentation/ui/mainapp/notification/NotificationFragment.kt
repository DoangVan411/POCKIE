package com.example.pockie.presentation.ui.mainapp.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pockie.R
import com.example.pockie.databinding.FragmentNotificationBinding
import com.example.pockie.domain.model.Notification

class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    private val sampleList = listOf(
        Notification(),
        Notification(),
        Notification(),
        Notification(),
        Notification(),
        Notification()
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            btnSetting.setOnClickListener {
                //notification setting
            }
        }

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val adapter = NotificationAdapter(){}
        binding.rvNoti.adapter = adapter
        binding.rvNoti.layoutManager = LinearLayoutManager(requireContext())
        adapter.submitList(sampleList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}