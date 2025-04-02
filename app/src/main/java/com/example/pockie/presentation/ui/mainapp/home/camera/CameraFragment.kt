package com.example.pockie.presentation.ui.mainapp.home.camera

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.transition.Visibility
import com.example.pockie.R
import com.example.pockie.databinding.FragmentCameraBinding
import com.example.pockie.presentation.ui.mainapp.home.HomeFragment
import com.example.pockie.presentation.ui.mainapp.home.HomeFragmentDirections
import dagger.hilt.android.components.ViewWithFragmentComponent

class CameraFragment : Fragment() {
    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CameraViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.takePhoto.setOnClickListener{
            updateUI(true)
        }

        binding.cancel.setOnClickListener {
            updateUI(false)
        }

        binding.send.setOnClickListener {
            updateUI(false)
        }
    }

    private fun updateUI(isVisibility: Boolean){
        binding.caption.visibility = if(isVisibility) View.VISIBLE else View.INVISIBLE
        binding.cancel.visibility = if(isVisibility) View.VISIBLE else View.INVISIBLE
        binding.send.visibility = if(isVisibility) View.VISIBLE else View.INVISIBLE
        binding.takePhoto.visibility = if(isVisibility) View.INVISIBLE else View.VISIBLE
        binding.reverse.visibility = if(isVisibility) View.INVISIBLE else View.VISIBLE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}