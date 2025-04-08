package com.example.pockie.presentation.ui.authentication.password

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pockie.R
import com.example.pockie.databinding.FragmentPasswordBinding
import com.example.pockie.presentation.utils.networkstate.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PasswordFragment : Fragment() {
    private var _binding: FragmentPasswordBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PasswordViewModel by viewModels()
    private val args: PasswordFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPasswordBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val status = args.status

        binding.next.setOnClickListener {
            if (status == "Sign Up") {
                if (checkPasswordSignUpValid()) signUp()
            } else {
                if(checkPasswordSignInValid()) signIn()
            }

        }

        if (status == "Sign In") {
            binding.forgotPasswd.visibility = View.VISIBLE
            binding.note.visibility = View.INVISIBLE
        }

        binding.forgotPasswd.setOnClickListener {
            // Email Authen
        }

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun checkPasswordSignUpValid(): Boolean {
        val passwd = binding.passwd.text.toString()
        val isLengthValid = passwd.length >= 8
        val hasUpperCase = passwd.any { it.isUpperCase() }
        val hasLowerCase = passwd.any { it.isLowerCase() }
        val hasDigit = passwd.any { it.isDigit() }

        if (isLengthValid && hasUpperCase && hasLowerCase && hasDigit) {
            binding.passwdLayout.helperText = ""
            binding.passwd.setBackgroundResource(R.drawable.background_field)
            return true
        } else {
            binding.passwdLayout.helperText = "Please enter valid password."
            binding.passwd.setBackgroundResource(R.drawable.background_field_error)
            return false
        }
    }

    private fun checkPasswordSignInValid(): Boolean{
        val passwd = binding.passwd.text.toString()
        if(passwd.isNullOrEmpty()) {
            binding.passwdLayout.helperText = "Password can not be blank"
            binding.passwd.setBackgroundResource(R.drawable.background_field_error)
            return false
        }
        else {
            binding.passwdLayout.helperText = ""
            binding.passwd.setBackgroundResource(R.drawable.background_field)
            return true
        }
    }

    private fun signIn() {
        val email = args.email
        viewModel.signIn(email, binding.passwd.text.toString())
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.signInState.collect { value ->
                when (value) {
                    is NetworkState.Loading, is NetworkState.Init -> {
                        binding.loading.visibility = View.VISIBLE
                        binding.next.isEnabled = false
                    }

                    is NetworkState.Success<*> -> {
                        findNavController().navigate(
                            PasswordFragmentDirections.actionPasswordFragmentToHomeFragment(),
                            navOptions = NavOptions.Builder().setPopUpTo(
                                R.id.passwordFragment, true
                            ).setPopUpTo(R.id.emailFragment, true)
                                .setPopUpTo(R.id.onboardingFragment, true).build()
                        )
                    }

                    is NetworkState.Error -> {
                        binding.loading.visibility = View.INVISIBLE
                        binding.next.isEnabled = true
                        Toast.makeText(
                            requireContext(),
                            value.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun signUp() {
        val email = args.email
        val password = binding.passwd.text.toString()
        viewModel.signUp(email, password)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.signUpState.collect { value ->
                when (value) {
                    is NetworkState.Loading, is NetworkState.Init -> {
                        binding.loading.visibility = View.VISIBLE
                        binding.next.isEnabled = false
                    }

                    is NetworkState.Success<*> -> {
                        findNavController().navigate(
                            PasswordFragmentDirections.actionPasswordFragmentToNameUserFragment(email, password),
                            navOptions = NavOptions.Builder().setPopUpTo(
                                R.id.passwordFragment, true
                            ).setPopUpTo(R.id.emailFragment, true)
                                .setPopUpTo(R.id.onboardingFragment, true).build()
                        )
                    }

                    is NetworkState.Error -> {
                        Toast.makeText(
                            requireContext(),
                            value.message,
                            Toast.LENGTH_LONG
                        ).show()
                        binding.loading.visibility = View.INVISIBLE
                        binding.next.isEnabled = true
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