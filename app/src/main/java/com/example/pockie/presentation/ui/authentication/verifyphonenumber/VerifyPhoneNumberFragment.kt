package com.example.pockie.presentation.ui.authentication.verifyphonenumber

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pockie.R
import com.example.pockie.databinding.FragmentVerifyPhoneNumberBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyPhoneNumberFragment : Fragment() {
    private var _binding: FragmentVerifyPhoneNumberBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VerifyPhoneNumberViewModel by viewModels()
    private val args: VerifyPhoneNumberFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVerifyPhoneNumberBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val status = args.status
        binding.next.setOnClickListener{
//            if(status == "Sign Up") findNavController().navigate(VerifyPhoneNumberFragmentDirections.actionVerifyPhoneNumberFragmentToPasswordFragment("Sign Up"))
//            else {
//
//            }
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