package com.example.pockie.presentation.ui.mainactivity

import android.os.Bundle
import android.view.WindowInsets
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.pockie.presentation.ui.mainapp.profile.ProfileFragment
import com.example.pockie.R
import com.example.pockie.databinding.ActivityMainBinding
import com.example.pockie.presentation.ui.mainapp.chat.chatlist.ChatListFragment
import com.example.pockie.presentation.ui.mainapp.friends.FriendsFragment
import com.example.pockie.presentation.ui.mainapp.home.HomeFragment
import com.example.pockie.presentation.ui.mainapp.notification.NotificationFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}