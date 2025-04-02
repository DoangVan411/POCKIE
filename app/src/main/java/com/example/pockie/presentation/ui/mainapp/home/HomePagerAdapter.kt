package com.example.pockie.presentation.ui.mainapp.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pockie.presentation.ui.mainapp.home.camera.CameraFragment
import com.example.pockie.presentation.ui.mainapp.home.post.PostFragment

class HomePagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> CameraFragment()
            else -> PostFragment()
        }

    }
}