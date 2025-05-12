package com.example.pockie.presentation.ui.mainapp.home.post

import ImagePagerAdapter
import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.pockie.R
import com.example.pockie.databinding.FragmentPostBinding
import com.example.pockie.domain.model.Post
import com.example.pockie.domain.model.PostItem
import com.example.pockie.presentation.ui.mainapp.home.HomeFragment
import com.example.pockie.presentation.utils.OnPostItemClickListener
import com.example.pockie.presentation.utils.networkstate.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostFragment : Fragment(), OnPostItemClickListener {
    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PostViewModel by viewModels()
    private var initialY : Float = 0F

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPosts()
        checkPosition()

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun checkPosition(){
        val parentViewPager = requireParentFragment().view?.findViewById<ViewPager2>(R.id.parent_view_pager)
        binding.viewPager.getChildAt(0).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialY = event.y
                }
                MotionEvent.ACTION_MOVE -> {
                    val deltaY = event.y - initialY

                    if (binding.viewPager.currentItem == 0) {
                        if (deltaY < 50) {
                            parentViewPager?.isUserInputEnabled = false
                        } else if (deltaY > 50) {
                            parentViewPager?.isUserInputEnabled = true
                        }
                    } else {
                        parentViewPager?.isUserInputEnabled = false
                    }
                }
            }
            false
        }
    }

    private fun initPosts(){
        viewModel.getAllPost()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allPost.collectLatest{result ->
                when (result){
                    is NetworkState.Loading, is NetworkState.Init -> {
                        binding.loading.visibility = View.VISIBLE
                        binding.viewPager.visibility = View.INVISIBLE
                        binding.text.visibility = View.INVISIBLE
                    }
                    is NetworkState.Success<*> -> {
                        val viewPager = binding.viewPager
                        val adapter = ImagePagerAdapter(this@PostFragment)
                        viewPager.adapter = adapter
                        adapter.submitList(result.data as List<Post>)

                        if(result.data.size == 0){
                            binding.text.visibility = View.VISIBLE
                        }
                        else{
                            binding.text.visibility = View.INVISIBLE
                        }
                        binding.loading.visibility = View.INVISIBLE
                        binding.viewPager.visibility = View.VISIBLE

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

    override fun onPostItemClicked() {
        (parentFragment as HomeFragment).setCurrentPage(0)
    }
}