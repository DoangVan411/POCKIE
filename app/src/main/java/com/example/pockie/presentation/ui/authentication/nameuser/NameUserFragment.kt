package com.example.pockie.presentation.ui.authentication.nameuser

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pockie.R
import com.example.pockie.databinding.FragmentNameUserBinding
import com.example.pockie.presentation.utils.networkstate.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NameUserFragment : Fragment() {
    private var _binding: FragmentNameUserBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NameUserViewModel by viewModels()
    private val args: NameUserFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNameUserBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val email = args.email
        val password = args.password

        binding.next.setOnClickListener {
            val firstName = binding.firstName.text.toString().trim()
            val lastName = binding.lastName.text.toString().trim()
            if (checkName(firstName, lastName)) {
                viewModel.saveUserToFireStore(email, password, "$firstName $lastName")
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.saveDataState.collect { result ->
                        when (result) {
                            is NetworkState.Loading, is NetworkState.Init -> {
                                binding.loading.visibility = View.VISIBLE
                                binding.next.isEnabled = false
                            }

                            is NetworkState.Success<*> -> {
                                findNavController().navigate(
                                    NameUserFragmentDirections.actionNameUserFragmentToSetUpFragment(),
                                    navOptions = NavOptions.Builder().setPopUpTo(R.id.nameUserFragment, true).build()
                                )
                            }
                            is NetworkState.Error -> {
                                Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
                                binding.next.isEnabled = true
                                binding.loading.visibility = View.INVISIBLE
                            }
                        }
                    }
                }
            }
        }

    }

    private fun checkName(firstName: String, lastName: String): Boolean {
        val isFirstNameValid = firstName.isNotEmpty()
        val isLastNameValid = lastName.isNotEmpty()
        if (!isFirstNameValid) {
            binding.firstNameLayout.helperText = "Please enter valid first name"
            binding.firstName.setBackgroundResource(R.drawable.background_field_error)
            return false
        } else if (!isLastNameValid) {
            binding.firstNameLayout.helperText = ""
            binding.lastNameLayout.helperText = "Please enter valid last name"
            binding.lastName.setBackgroundResource(R.drawable.background_field_error)
            binding.firstName.setBackgroundResource(R.drawable.background_field)
            return false
        } else {
            binding.firstNameLayout.helperText = ""
            binding.lastNameLayout.helperText = ""
            binding.firstName.setBackgroundResource(R.drawable.background_field)
            binding.lastName.setBackgroundResource(R.drawable.background_field)
            return true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}