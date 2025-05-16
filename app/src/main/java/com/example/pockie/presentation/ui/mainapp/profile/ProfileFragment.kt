package com.example.pockie.presentation.ui.mainapp.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pockie.databinding.FragmentProfileBinding
import com.example.pockie.domain.model.Post
import java.util.Date

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

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

        with(binding) {
            toolbar.setOnClickListener{
                findNavController().popBackStack()
            }
            btnShare.setOnClickListener {

            }
            btnAddFriend.setOnClickListener {
//                findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
            }
        }

        setUpRecyclerView()
    }

    private fun setUpRecyclerView () {
        val sampleList = listOf(
            Post("", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSRP9fy5-QNpKRSWAtMw1WqC4twnkQyJbaXjA&s", "", Date(), ""),
            Post("", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSRP9fy5-QNpKRSWAtMw1WqC4twnkQyJbaXjA&s", "", Date(), ""),
            Post("", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSRP9fy5-QNpKRSWAtMw1WqC4twnkQyJbaXjA&s", "", Date(), ""),
            Post("", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSRP9fy5-QNpKRSWAtMw1WqC4twnkQyJbaXjA&s", "", Date(), ""),
            Post("", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSRP9fy5-QNpKRSWAtMw1WqC4twnkQyJbaXjA&s", "", Date(), ""),
            Post("", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSRP9fy5-QNpKRSWAtMw1WqC4twnkQyJbaXjA&s", "", Date(), ""),
            Post("", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSRP9fy5-QNpKRSWAtMw1WqC4twnkQyJbaXjA&s", "", Date(), ""),
            Post("", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSRP9fy5-QNpKRSWAtMw1WqC4twnkQyJbaXjA&s", "", Date(), ""),
        )
        val profileAdapter = ProfileAdapter(){}
        binding.rvPost.adapter = profileAdapter
        binding.rvPost.layoutManager = GridLayoutManager(requireContext(), 4)
        profileAdapter.submitList(sampleList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}