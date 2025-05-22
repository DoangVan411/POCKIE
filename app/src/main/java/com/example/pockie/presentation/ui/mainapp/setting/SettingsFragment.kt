package com.example.pockie.presentation.ui.mainapp.setting

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.pockie.R
import com.example.pockie.databinding.FragmentSettingsBinding
import com.example.pockie.presentation.utils.networkstate.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            layoutInfo.setOnClickListener {
                findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToIntroductionFragment())
            }
        }



        setupSignOut()
        setupDeleteAccount()
        observeDeleteAccountState()
    }

    private fun setupSignOut() {
        binding.layoutSignOut.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Do you want to sign out?")
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Yes") { dialog, _ ->
                    viewModel.signOut()
                    dialog.dismiss()
                    navigateToOnboarding()
                }
                .show()
        }
    }

    private fun setupDeleteAccount() {
        binding.layoutDeleteAccount.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Do you want to delete this account?")
                .setMessage("This action cannot be undone!")
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Delete") { dialog, _ ->
                    viewModel.deleteAccount()
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun observeDeleteAccountState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.deleteAccountState.collect { state ->
                when (state) {
                    is NetworkState.Success<*> -> {
                        Toast.makeText(requireContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show()
                        navigateToOnboarding()
                    }
                    is NetworkState.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun navigateToOnboarding() {
        findNavController().navigate(
            R.id.onboardingFragment,
            null,
            NavOptions.Builder()
                .setPopUpTo(R.id.onboarding_graph, true)
                .build()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}