package com.example.pockie.presentation.ui.mainapp.friends.requests

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pockie.R
import com.example.pockie.databinding.FragmentRequestPageBinding
import com.example.pockie.domain.model.User
import com.example.pockie.presentation.ui.friends.requests.RequestPageViewModel
import com.example.pockie.presentation.ui.mainapp.friends.friends.FriendsPageAdapter

class RequestPage : Fragment() {

    private var _binding: FragmentRequestPageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RequestPageViewModel by viewModels()

    private lateinit var adapterSent: RequestsPageAdapter
    private lateinit var adapterReceive: RequestsPageAdapter

    private val list1 = listOf(
        User("vandoan", "Vân Đoàn", R.drawable.setting, "Hello", "1"),
        User("vandoan", "Vân Đoàn", R.drawable.setting, "Hello", "1"),
        User("vandoan", "Vân Đoàn", R.drawable.setting, "Hello", "1"),
        User("vandoan", "Vân Đoàn", R.drawable.setting, "Hello", "1"),
        User("vandoan", "Vân Đoàn", R.drawable.setting, "Hello", "1"),
        User("vandoan", "Vân Đoàn", R.drawable.setting, "Hello", "1"),
        User("vandoan", "Vân Đoàn", R.drawable.setting, "Hello", "1"),
        User("vandoan", "Vân Đoàn", R.drawable.setting, "Hello", "1"),
        User("vandoan", "Vân Đoàn", R.drawable.setting, "Hello", "1"),
        User("vandoan", "Vân Đoàn", R.drawable.setting, "Hello", "1"),
        User("vandoan", "Vân Đoàn", R.drawable.setting, "Hello", "1"),
        User("vandoan", "Vân Đoàn", R.drawable.setting, "Hello", "1"),
        User("vandoan", "Vân Đoàn", R.drawable.setting, "Hello", "1"),
        User("vandoan", "Vân Đoàn", R.drawable.setting, "Hello", "1"),
        User("vandoan", "Vân Đoàn", R.drawable.setting, "Hello", "1"),

        )
    private val list2 = listOf(
        User("vandoan", "Vân Đoàng", R.drawable.setting, "Hello", "1"),
        User("vandoan", "Vân Đoàng", R.drawable.setting, "Hello", "1"),
        User("vandoan", "Vân Đoàng", R.drawable.setting, "Hello", "1"),
        User("vandoan", "Vân Đoàng", R.drawable.setting, "Hello", "1"),
        User("vandoan", "Vân Đoàng", R.drawable.setting, "Hello", "1"),
        User("vandoan", "Vân Đoàng", R.drawable.setting, "Hello", "1"),
        User("vandoan", "Vân Đoàng", R.drawable.setting, "Hello", "1"),
        User("vandoan", "Vân Đoàng", R.drawable.setting, "Hello", "1"),
        User("vandoan", "Vân Đoàng", R.drawable.setting, "Hello", "1"),

    )

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

        binding.tvRequestReceive.text = "Received request (${list1.size})"
        binding.tvRequestSent.text = "Sent request (${list2.size})"

        setUpRecyclerView()
        setUpListener()
    }

    private fun setUpRecyclerView() {
        adapterSent = RequestsPageAdapter(){
            //on item click
        }
        adapterReceive = RequestsPageAdapter(){
            //on item click
        }
        binding.rvRequestSent.adapter = adapterSent
        binding.rvRequestReceive.adapter = adapterReceive
        binding.rvRequestSent.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRequestReceive.layoutManager = LinearLayoutManager(requireContext())

        adapterSent.submitList(list2)
        adapterReceive.submitList(list1)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}