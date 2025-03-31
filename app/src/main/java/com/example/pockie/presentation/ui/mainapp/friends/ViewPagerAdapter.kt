package com.example.pockie.presentation.ui.mainapp.friends

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pockie.presentation.ui.mainapp.friends.friends.FriendsPage
import com.example.pockie.presentation.ui.mainapp.friends.requests.RequestPage

class ViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FriendsPage()
            else -> RequestPage()
        }
    }

}