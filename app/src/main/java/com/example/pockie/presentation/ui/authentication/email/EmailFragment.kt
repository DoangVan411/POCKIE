package com.example.pockie.presentation.ui.authentication.email

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pockie.R
import com.example.pockie.databinding.FragmentEmailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmailFragment : Fragment() {
    private var _binding: FragmentEmailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EmailViewModel by viewModels()
    private val args: EmailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val status = args.status

        binding.next.setOnClickListener {
            if (checkValidEmail()) {
                findNavController().navigate(EmailFragmentDirections.actionEmailFragmentToPasswordFragment(status, binding.email.text.toString()))
            }
        }

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun checkValidEmail(): Boolean {
        if (binding.email.text.isNullOrEmpty()) {
            binding.emailLayout.helperText = "Please enter your email."
            binding.email.setBackgroundResource(R.drawable.background_field_error)
            return false
        } else {
            if (Patterns.EMAIL_ADDRESS.matcher(binding.email.text.toString()).matches()) {
                binding.emailLayout.helperText = ""
                binding.email.setBackgroundResource(R.drawable.background_field)
                return true
            } else {
                binding.emailLayout.helperText = "Please enter valid email."
                binding.email.setBackgroundResource(R.drawable.background_field_error)
                return false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}