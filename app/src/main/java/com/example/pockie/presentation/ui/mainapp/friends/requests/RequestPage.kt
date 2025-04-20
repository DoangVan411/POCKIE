package com.example.pockie.presentation.ui.mainapp.friends.requests

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pockie.R
import com.example.pockie.databinding.FragmentRequestPageBinding
import com.example.pockie.domain.model.Account
import com.example.pockie.presentation.utils.networkstate.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RequestPage : Fragment() {

    private var _binding: FragmentRequestPageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RequestPageViewModel by viewModels()

    private lateinit var adapterSent: SentRequestsAdapter
    private lateinit var adapterReceive: ReceivedRequestAdapter
    private lateinit var adapterSearch: FindUserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRequestPageBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.getSentRequests()
        viewModel.getReceivedRequests()
        viewModel.getFriends()
        setUpRecyclerView()
        setUpListener()
        collectSearchUser()

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

        collectRequests()
        collectFriends()
    }

    private fun collectRequests() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sentRequestsState.collectLatest {state ->
                    when (state) {
                        is NetworkState.Init -> {}
                        is NetworkState.Loading -> {
                            Log.d("Progress", "Loading")
                            binding.progressBar2.visibility = View.VISIBLE
                            binding.rvRequestSent.visibility = View.GONE
                        }
                        is NetworkState.Success<*> -> {
                            binding.progressBar2.visibility = View.GONE
                            binding.rvRequestSent.visibility = View.VISIBLE
                            val sentRequests = state.data as? List<Account> ?: emptyList()
                            Log.d("Requests sent", "${sentRequests.size}")
                            binding.tvRequestSent.text = "Sent requests: ${sentRequests.size}"
                            adapterSent.submitList(sentRequests)
                        }
                        is NetworkState.Error -> {
                            binding.progressBar2.visibility = View.GONE
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.receivedRequestsState.collectLatest {state ->
                    when (state) {
                        is NetworkState.Init -> {}
                        is NetworkState.Loading -> {
                            Log.d("Progress", "Loading")
                            binding.progressBar1.visibility = View.VISIBLE
                            binding.rvRequestReceive.visibility = View.GONE
                        }
                        is NetworkState.Success<*> -> {
                            binding.progressBar1.visibility = View.GONE
                            binding.rvRequestReceive.visibility = View.VISIBLE
                            val receivedRequests = state.data as? List<Account> ?: emptyList()
                            Log.d("Requests received", "${receivedRequests.size}")
                            binding.tvRequestReceive.text = "Received requests: ${receivedRequests.size}"
                            adapterReceive.submitList(receivedRequests)
                        }
                        is NetworkState.Error -> {
                            binding.progressBar1.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun collectFriends() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.friendsState.collectLatest { state ->
                    when (state) {
                        is NetworkState.Init -> {}
                        is NetworkState.Loading -> {
                            Log.d("Progress", "Loading")
                        }

                        is NetworkState.Success<*> -> {
                        }

                        is NetworkState.Error -> {
                        }
                    }
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        adapterSent = SentRequestsAdapter({
        }){
            //on item click
        }
        adapterReceive = ReceivedRequestAdapter({
            viewModel.acceptRequest(it.uid)
        }){
            //on item click
        }
        adapterSearch = FindUserAdapter ({
            viewModel.sendRequest(it.uid)
        }){

        }
        with(binding) {
            rvRequestSent.adapter = adapterSent
            rvRequestReceive.adapter = adapterReceive
            rvFindUsers.adapter = adapterSearch
            rvRequestSent.layoutManager = LinearLayoutManager(requireContext())
            rvRequestReceive.layoutManager = LinearLayoutManager(requireContext())
            rvFindUsers.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setUpListener () {
        with(binding) {
            btnShowMoreSent.setOnClickListener {
                adapterSent.toggleExpand()
                btnShowMoreSent.text = if (adapterSent.itemCount > 5)
                    getString(R.string.show_less)
                else
                    getString(R.string.show_more)
            }
            btnShowMoreReceived.setOnClickListener {
                adapterReceive.toggleExpand()
                btnShowMoreReceived.text = if (adapterReceive.itemCount > 5)
                    getString(R.string.show_less)
                else
                    getString(R.string.show_more)
            }
        }
    }

    private fun collectSearchUser() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchResultsWithStatus.collect {searchResults ->
                    if (searchResults.isNotEmpty()) {
                        binding.rvFindUsers.visibility = View.VISIBLE
                    } else {
                        binding.rvFindUsers.visibility = View.GONE
                    }
                    adapterSearch.submitList(searchResults)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}