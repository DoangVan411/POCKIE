package com.example.pockie.presentation.ui.mainapp.home.post

import ImagePagerAdapter
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.pockie.R
import com.example.pockie.databinding.FragmentPostBinding
import com.example.pockie.domain.model.Post
import com.example.pockie.domain.model.PostItem
import com.example.pockie.presentation.ui.mainapp.home.HomeFragment
import com.example.pockie.presentation.utils.OnPostItemClickListener
import com.example.pockie.presentation.utils.Utils
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
    private var initialY: Float = 0F

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSpinner()
        checkPosition()

    }

    private fun initSpinner(){
        val tagNames = listOf("All", "Other", "Study", "Entertainment", "Food", "School")
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item, tagNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                val selectedTag = position
                initPosts(selectedTag)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun checkPosition() {
        val parentViewPager =
            requireParentFragment().view?.findViewById<ViewPager2>(R.id.parent_view_pager)
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

    private fun initPosts(tagId: Int) {
        viewModel.getAllPost(tagId)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allPost.collectLatest { result ->
                when (result) {
                    is NetworkState.Loading, is NetworkState.Init -> {
                        binding.loading.visibility = View.VISIBLE
                        binding.viewPager.visibility = View.INVISIBLE
                        binding.text.visibility = View.INVISIBLE
                    }

                    is NetworkState.Success<*> -> {
                        val viewPager = binding.viewPager
                        val adapter = ImagePagerAdapter(
                            this@PostFragment, onLikeClicked = { post ->
                                updatePost(post)
                            },
                            onDownload = { post ->
                                downloadImg(post)
                            },
                            onReplyPost = { message, post ->
                                replyPost(message, post)
                            },
                            onDelete = {
                                deletePost(it)
                            })
                        viewPager.adapter = adapter
                        adapter.submitList(result.data as List<PostItem>)

                        if (result.data.size == 0) {
                            binding.text.visibility = View.VISIBLE
                        } else {
                            binding.text.visibility = View.INVISIBLE
                        }
                        binding.loading.visibility = View.INVISIBLE
                        binding.viewPager.visibility = View.VISIBLE

                    }

                    else -> {
                        Toast.makeText(requireContext(), result.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun updatePost(post: Post) {
        viewModel.updatePost(post)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.updatePost.collectLatest { result ->
                when (result) {
                    is NetworkState.Error -> {
                        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> {
                        Log.d("Post", "Success")
                    }
                }
            }
        }
    }

    private fun downloadImg(post: Post){
        val request = DownloadManager.Request(Uri.parse(post.imageUrl)).apply {
            setTitle("Downloading Image")
            setDescription("Downloading ${post.id}.jpg")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "${post.id}.jpg")
            setAllowedOverMetered(true)
            setAllowedOverRoaming(true)
        }

        val downloadManager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }

    private fun replyPost(message: String, post: Post){
        viewModel.replyPost(message, post)
    }

    private fun deletePost(post: Post){
        AlertDialog.Builder(requireContext())
            .setTitle("Do you want to delete post")
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Yes") { dialog, _ ->
                viewModel.deletePost(post)
                dialog.dismiss()
                Toast.makeText(requireContext(), "Delete successfully!", Toast.LENGTH_SHORT).show()
            }
            .show()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPostItemClicked() {
        (parentFragment as HomeFragment).setCurrentPage(0)
    }

}