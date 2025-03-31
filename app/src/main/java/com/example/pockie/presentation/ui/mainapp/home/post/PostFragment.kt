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
import androidx.viewpager2.widget.ViewPager2
import com.example.pockie.R
import com.example.pockie.databinding.FragmentPostBinding
import com.example.pockie.domain.model.PostItem
import com.example.pockie.presentation.ui.mainapp.home.HomeFragment
import com.example.pockie.presentation.utils.OnPostItemClickListener

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

        val imageList: List<PostItem> = listOf(
            PostItem("https://i.pinimg.com/736x/85/5a/08/855a08497b0a69f996d2af9bf4953e67.jpg"),
            PostItem("https://i.pinimg.com/736x/22/0f/6c/220f6cbc6f135011614cf55535488217.jpg"),
            PostItem("https://i.pinimg.com/474x/e7/fd/97/e7fd9732edcda738001baaf904231e15.jpg"),
            PostItem("https://i.pinimg.com/736x/e4/a8/c7/e4a8c77e92259cd10fbedad9f579c8cf.jpg"),
            PostItem("https://i.pinimg.com/236x/ae/7e/4d/ae7e4d7ae283177b77e4c7e3f4ecbf97.jpg"),
            PostItem("https://i.pinimg.com/236x/12/5c/5a/125c5a3f361d6d9496655ba7cf1ebc1e.jpg"),
            PostItem("https://i.pinimg.com/236x/36/4b/46/364b463739c4954fb7ee1a88297fe40f.jpg"),
            PostItem("https://i.pinimg.com/474x/8e/3a/ea/8e3aea51809f796e049f34a28f41d8be.jpg")
        )
        val viewPager = binding.viewPager
        val adapter = ImagePagerAdapter(this)
        viewPager.adapter = adapter
        adapter.submitList(imageList)
        checkPosition()

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun checkPosition(){
        val parentViewPager = requireParentFragment().view?.findViewById<ViewPager2>(R.id.parent_view_pager)
        binding.viewPager.getChildAt(0).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d("viewpager", event.y.toString())
                    initialY = event.y
                }
                MotionEvent.ACTION_MOVE -> {
                    val deltaY = event.y - initialY

                    Log.d("viewpager", deltaY.toString())
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPostItemClicked() {
        (parentFragment as HomeFragment)?.setCurrentPage(0)
    }
}