package com.example.pockie.presentation.ui.authentication.setup

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.pockie.R
import com.example.pockie.databinding.FragmentSetUpBinding
import com.example.pockie.presentation.ui.PermissionManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SetUpFragment : Fragment() {
    private var _binding: FragmentSetUpBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SetUpViewModel by viewModels()
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if(!isGranted){ //Quyền đã bị không cho phép hoặc bị chặn
            PermissionManager.handlePermissionResult(requireContext(), Manifest.permission.POST_NOTIFICATIONS, false)
        }
    }

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if(!isGranted){
            PermissionManager.handlePermissionResult(requireContext(), Manifest.permission.CAMERA, false)
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetUpBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PermissionManager.requestPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS, notificationPermissionLauncher )
        }
        PermissionManager.requestPermission(requireContext(), Manifest.permission.CAMERA, cameraPermissionLauncher)

        binding.finish.setOnClickListener {
            findNavController().navigate(
                SetUpFragmentDirections.actionSetUpFragmentToHomeFragment(),
                NavOptions.Builder().setPopUpTo(R.id.setUpFragment, true).build()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}