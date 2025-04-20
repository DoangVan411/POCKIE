package com.example.pockie.presentation.ui.mainapp.friends.friends

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pockie.databinding.FragmentFriendsPageBinding
import com.example.pockie.domain.model.Account
import com.example.pockie.presentation.ui.friends.friends.FriendsPageViewModel
import com.example.pockie.presentation.utils.networkstate.NetworkState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FriendsPage : Fragment() {

    private var _binding: FragmentFriendsPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FriendsPageAdapter

    private val viewModel: FriendsPageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendsPageBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getFriends()
        setUpRecyclerView()
        collectFriends()

    }

    private fun setUpRecyclerView() {
        adapter = FriendsPageAdapter({
            AlertDialog.Builder(requireContext())
                .setTitle("Unfriend ${it.fullName}?")
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Yes") { dialog, _ ->
                    viewModel.deleteFriend(it.uid)
                    dialog.dismiss()
                }
                .show()
        }){}
        binding.rvFriends.adapter = adapter
        binding.rvFriends.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun collectFriends() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.friendsState.collectLatest { state ->
                    when (state) {
                        is NetworkState.Init -> {}
                        is NetworkState.Loading -> {
                            Log.d("Progress", "Loading")
                            binding.progressBar.visibility = View.VISIBLE
                            binding.rvFriends.visibility = View.GONE
                        }
                        is NetworkState.Success<*> -> {
                            binding.progressBar.visibility = View.GONE
                            binding.rvFriends.visibility = View.VISIBLE
                            val friends = state.data as? List<Account> ?: emptyList()
                            Log.d("Messages", "${friends.size}")
                            Log.d("FriendsPage", "Friend list size: ${friends.size}")
                            binding.tvFriendsCount.text = "Friends: ${friends.size}"
                            adapter.submitList(friends)
                        }
                        is NetworkState.Error -> {
                            binding.progressBar.visibility = View.GONE
                        }
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