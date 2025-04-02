package com.example.pockie.presentation.ui.mainapp.home.post.postitem

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import com.example.pockie.R
import com.example.pockie.databinding.FragmentPostItemBinding
import com.example.pockie.domain.model.PostItem

class PostItemFragment : Fragment() {
    private var _binding: FragmentPostItemBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PostItemViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostItemBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}