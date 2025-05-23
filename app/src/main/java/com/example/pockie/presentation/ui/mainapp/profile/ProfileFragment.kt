package com.example.pockie.presentation.ui.mainapp.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.pockie.databinding.FragmentProfileBinding
import com.example.pockie.domain.model.Post
import com.example.pockie.presentation.utils.networkstate.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()
    private val args: ProfileFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uid = args.uid
        with(binding) {
            toolbar.setOnClickListener{
                findNavController().popBackStack()
            }
        }

        if(!uid.isNullOrEmpty()) binding.btnAddFriend.visibility = View.GONE
        init(uid)
        setUpRecyclerView(uid)

    }

    private fun init(uid: String){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getAccount(uid)
            viewModel.account.collectLatest { account ->
                Glide.with(requireContext()).load(account.avtUrl).into(binding.ivAvatar)
                binding.tvName.text = account.fullName
                if(account.bio != "") {
                    binding.tvBio.text = account.bio
                } else {
                    binding.tvBio.text = "No bio"
                }
                binding.btnAddFriend.setOnClickListener {
                    findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment(account.fullName, account.bio, account.avtUrl))
                }
            }
        }
    }

    private fun setUpRecyclerView (uid: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getPosts(uid)
            viewModel.posts.collectLatest { result ->
                when(result){
                    is NetworkState.Init, is NetworkState.Loading -> {
                        binding.loading.visibility = View.VISIBLE
                    }
                    is NetworkState.Success<*> -> {
                        binding.loading.visibility = View.INVISIBLE
                        val profileAdapter = ProfileAdapter(){}
                        binding.rvPost.adapter = profileAdapter
                        binding.rvPost.layoutManager = GridLayoutManager(requireContext(), 3)
                        profileAdapter.submitList(result.data as List<Post>)
                        if(result.data.size == 0){
                            binding.note.visibility = View.VISIBLE
                        }
                    }
                    else -> {
                        Toast.makeText(requireContext(), result.toString(), Toast.LENGTH_SHORT).show()
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