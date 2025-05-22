package com.example.pockie.presentation.ui.mainapp.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.transition.Visibility
import androidx.viewpager2.widget.ViewPager2
import com.example.pockie.R
import com.example.pockie.databinding.FragmentHomeBinding
import com.example.pockie.presentation.ui.PermissionManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if(!isGranted){
            PermissionManager.handlePermissionResult(requireContext(), Manifest.permission.CAMERA, false)
        }
    }
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if(!isGranted){
            PermissionManager.handlePermissionResult(requireContext(), Manifest.permission.POST_NOTIFICATIONS, false)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Check quyền camera + notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PermissionManager.requestPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS, notificationPermissionLauncher )
        }
        PermissionManager.requestPermission(requireContext(), Manifest.permission.CAMERA, cameraPermissionLauncher)

        val viewPager = binding.parentViewPager
        val adapter = HomePagerAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(position == 1){
                    binding.navBar.visibility = View.INVISIBLE
                }
                else{
                    binding.navBar.visibility = View.VISIBLE
                }
            }
        })

        binding.profile.setOnClickListener{
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProfileFragment(""))
        }

        binding.chat.setOnClickListener{
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToChatListFragment())
        }

        binding.friend.setOnClickListener{
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToFriendsFragment())
        }

        binding.settings.setOnClickListener{
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSettingsFragment())
        }

    }

    fun setCurrentPage(page: Int) {
        binding.parentViewPager.currentItem = page
        binding.parentViewPager.isUserInputEnabled = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}