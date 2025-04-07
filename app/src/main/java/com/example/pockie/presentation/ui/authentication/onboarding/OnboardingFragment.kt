package com.example.pockie.presentation.ui.authentication.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.pockie.R
import com.example.pockie.databinding.FragmentOnboardingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment : Fragment() {
    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OnboardingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnboardingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(viewModel.checkLogin()){
            findNavController().navigate(OnboardingFragmentDirections.actionOnboardingFragmentToHomeFragment(), navOptions = NavOptions.Builder().setPopUpTo(
                R.id.onboardingFragment, true).build())
        }

        binding.signIn.setOnClickListener{
            findNavController().navigate(OnboardingFragmentDirections.actionOnboardingFragmentToEmailFragment("Sign In"))
        }

        binding.signUp.setOnClickListener{
            findNavController().navigate(OnboardingFragmentDirections.actionOnboardingFragmentToEmailFragment("Sign Up"))
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}