package com.example.pockie.presentation.ui.mainapp.friends.friends

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pockie.databinding.FragmentFriendsPageBinding
import com.example.pockie.domain.model.Account
import com.example.pockie.presentation.ui.friends.friends.FriendsPageViewModel
import com.example.pockie.presentation.ui.mainapp.friends.FriendsFragment
import com.example.pockie.presentation.utils.FriendsPageNavigator
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
    private var navigator: FriendsPageNavigator? = null
    private val viewModel: FriendsPageViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigator = when {
            parentFragment is FriendsPageNavigator -> parentFragment as FriendsPageNavigator
            context is FriendsPageNavigator -> context
            else -> null
        }
    }

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

        val searchEditText = binding.searchView.findViewById<EditText>(
            androidx.appcompat.R.id.search_src_text
        )
        searchEditText.setTextColor(Color.WHITE)
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.onSearchQueryChanged(newText.orEmpty())
                return true
            }
        })

        setUpRecyclerView()
        collectFriends()

    }

    private fun setUpRecyclerView() {
        adapter = FriendsPageAdapter(onButtonRemoveClick = {
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
        }, onItemClick = {
            navigator?.navigateToFriendDetail(it.uid)
        })
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