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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pockie.R
import com.example.pockie.databinding.FragmentEmailBinding
import com.example.pockie.presentation.utils.networkstate.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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

        if(status == "ForgotPass"){
            binding.text.text = "Please enter email to retrieve password"
        }

        binding.next.setOnClickListener {
            if (checkValidEmail()){
                if(status == "ForgotPass"){
                    viewModel.resetPassword(binding.email.text.toString())
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.resetPasswordState.collect{result ->
                            when(result){
                                is NetworkState.Loading, is NetworkState.Init -> {
                                    binding.loading.visibility = View.VISIBLE
                                    binding.next.isEnabled = false
                                }
                                is NetworkState.Success<*> -> {
                                    Toast.makeText(requireContext(), result.data.toString(), Toast.LENGTH_LONG).show()
                                    binding.loading.visibility = View.INVISIBLE
                                    findNavController().popBackStack()
                                }
                                is NetworkState.Error -> {
                                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
                                    binding.loading.visibility = View.INVISIBLE
                                    binding.next.isEnabled = true
                                }
                            }
                        }
                    }
                }
                else{
                    findNavController().navigate(EmailFragmentDirections.actionEmailFragmentToPasswordFragment(status, binding.email.text.toString()))
                }
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