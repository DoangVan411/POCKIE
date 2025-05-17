package com.example.pockie.presentation.ui.mainapp.profile.edit_profile

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.pockie.databinding.FragmentEditProfileBinding
import com.example.pockie.databinding.FragmentProfileBinding
import com.example.pockie.presentation.utils.networkstate.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProfileFragment: Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditProfileViewModel by viewModels()
    private val args: EditProfileFragmentArgs by navArgs()
    private lateinit var uriAvt: Uri
    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                Log.d("Post", uri.toString())
                uriAvt = uri
                Glide.with(requireContext()).load(uri).into(binding.ivAvatar)
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fullName = args.fullName
        val bio = args.bio
        val avtUrl = args.avtUrl

        setUp(fullName, bio, avtUrl)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnChangeAvatar.setOnClickListener {
            selectAvt()
        }

        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnSave.setOnClickListener {
            generateAvtLinkPhoto()
        }

    }

    private fun setUp(fullName: String, bio: String, avtUrl: String){
        if(fullName.isNotEmpty()){
            binding.etFullName.setText(fullName)
        }
        if(bio.isNotEmpty()){
            binding.etBio.setText(bio)
        }
        if(avtUrl.isNotEmpty()){
            Glide.with(requireContext()).load(avtUrl).into(binding.ivAvatar)
        }
    }

    private fun selectAvt(){
        imagePickerLauncher.launch("image/*")
    }

    private fun generateAvtLinkPhoto(){
        val fileName = "avatar_${System.currentTimeMillis()}.jpg"
        viewModel.generateAvtLinkPhoto(fileName, uriAvt, requireContext())
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.generateAvtLinkPhoto.collectLatest { state ->
                when (state){
                    is NetworkState.Init, is NetworkState.Loading -> {
                        binding.btnSave.isEnabled = false
                        binding.loading.visibility = View.VISIBLE
                    }
                    is NetworkState.Success <*> -> {
                        Log.d("Post", state.data.toString())
                        saveProfile(state.data.toString())
                    }
                    else -> {
                        binding.btnSave.isEnabled = false
                        binding.loading.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), state.toString(), Toast.LENGTH_SHORT).show()
                        Log.d("Post", state.toString())
                    }
                }
            }
        }
    }

    private fun saveProfile(avtUrl: String) {
        val fullName = binding.etFullName.text.toString()
        val bio = binding.etBio.text.toString()

        Log.d("Post", avtUrl)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.editProfile(fullName, bio, avtUrl)
            viewModel.edit.collectLatest { state ->
                when(state){
                    is NetworkState.Init, is NetworkState.Loading -> {
                        binding.loading.visibility = View.VISIBLE
                        binding.btnSave.isEnabled = false
                    }
                    is NetworkState.Success<*> -> {
                        Toast.makeText(requireContext(), state.data.toString(), Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
                    else -> {
                        Toast.makeText(requireContext(), state.toString(), Toast.LENGTH_SHORT).show()
                        binding.btnSave.isEnabled = true
                        binding.loading.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}