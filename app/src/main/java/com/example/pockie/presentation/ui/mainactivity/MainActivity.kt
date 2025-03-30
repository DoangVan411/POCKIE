package com.example.pockie.presentation.ui.mainactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pockie.presentation.ui.profile.edit_profile.EditProfileFragment
import com.example.pockie.R
import com.example.pockie.databinding.ActivityMainBinding
import com.example.pockie.presentation.ui.chat.singlechat.SingleChatFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            val fragment = SingleChatFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.main, fragment)
                .commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}