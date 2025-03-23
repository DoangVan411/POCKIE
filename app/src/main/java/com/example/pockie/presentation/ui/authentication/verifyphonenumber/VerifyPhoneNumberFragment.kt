package com.example.pockie.presentation.ui.authentication.verifyphonenumber

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.pockie.R
import com.example.pockie.databinding.FragmentVerifyPhoneNumberBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyPhoneNumberFragment : Fragment() {
    private var _binding: FragmentVerifyPhoneNumberBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VerifyPhoneNumberViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVerifyPhoneNumberBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.next.setOnClickListener{
            findNavController().navigate(VerifyPhoneNumberFragmentDirections.actionVerifyPhoneNumberFragmentToPasswordFragment())
        }
        binding.back.setOnClickListener{
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}