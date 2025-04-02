package com.example.pockie.presentation.ui.mainapp.home

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.transition.Visibility
import androidx.viewpager2.widget.ViewPager2
import com.example.pockie.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = binding.parentViewPager
        val adapter = HomePagerAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(position == 1){
                    binding.navBar.visibility = View.GONE
                }
                else{
                    binding.navBar.visibility = View.VISIBLE
                }
            }
        })


        binding.profile.setOnClickListener{
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProfileFragment())
        }

        binding.chat.setOnClickListener{
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToChatListFragment())
        }

        binding.friend.setOnClickListener{
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToFriendsFragment())
        }

        binding.notification.setOnClickListener{
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToNotificationFragment())
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