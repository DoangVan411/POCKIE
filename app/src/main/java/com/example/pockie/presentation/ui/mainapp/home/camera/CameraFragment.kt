package com.example.pockie.presentation.ui.mainapp.home.camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.pockie.databinding.FragmentCameraBinding
import com.example.pockie.domain.model.Post
import com.example.pockie.presentation.utils.networkstate.NetworkState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.Date

@AndroidEntryPoint
class CameraFragment : Fragment() {
    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private var fileName: String? = null
    private var photoFile: File? = null
    private lateinit var imageCapture: ImageCapture
    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private val viewModel: CameraViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.takePhoto.setOnClickListener {
            binding.takePhoto.visibility = View.INVISIBLE
            binding.loading.visibility = View.VISIBLE
            binding.caption.text.clear()
            takePhoto()
        }

        binding.cancel.setOnClickListener {
            updateUI(false)
            binding.camera.visibility = View.VISIBLE
            binding.photo.visibility = View.INVISIBLE
        }

        binding.send.setOnClickListener {
            updateUI(false)
            generateLinkPhoto()
            binding.camera.visibility = View.VISIBLE
            binding.photo.visibility = View.INVISIBLE
        }

        binding.reverse.setOnClickListener {
            if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
            } else cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            startCamera()
        }

        startCamera()
        viewModel.getFCMToken()
    }

    private fun updateUI(isVisibility: Boolean) {
        binding.caption.visibility = if (isVisibility) View.VISIBLE else View.INVISIBLE
        binding.cancel.visibility = if (isVisibility) View.VISIBLE else View.INVISIBLE
        binding.send.visibility = if (isVisibility) View.VISIBLE else View.INVISIBLE
        binding.takePhoto.visibility = if (isVisibility) View.INVISIBLE else View.VISIBLE
        binding.reverse.visibility = if (isVisibility) View.INVISIBLE else View.VISIBLE
        binding.text.visibility = if(isVisibility) View.INVISIBLE else View.VISIBLE
        binding.iconHistory.visibility = if(isVisibility) View.INVISIBLE else View.VISIBLE
    }

    private fun startCamera() {
        val cameraProvideFuture = ProcessCameraProvider.getInstance(requireContext())
        imageCapture = ImageCapture.Builder().build()

        cameraProvideFuture.addListener({
            val cameraProvider = cameraProvideFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.camera.surfaceProvider)
                }
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
                Log.e("Camera", e.message.toString())
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto() {
        val name = "photo_${System.currentTimeMillis()}.jpg"
        val file = File(requireContext().filesDir, "photo_${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    fileName = name
                    photoFile = file

                    if (cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA) {
                        val originalBitmap = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                        val flippedBitmap = flipBitmapHorizontally(originalBitmap)

                        FileOutputStream(photoFile).use { out ->
                            flippedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                        }
                    }
                    Glide.with(binding.photo).load(photoFile).centerCrop().into(binding.photo)

                    binding.photo.visibility = View.VISIBLE
                    binding.camera.visibility = View.INVISIBLE
                    binding.loading.visibility = View.INVISIBLE
                    updateUI(true)
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(context, exception.message.toString(), Toast.LENGTH_SHORT).show()
                    Log.e("Camera", exception.message.toString())
                }

            })
    }

    private fun generateLinkPhoto() {
        viewModel.generateLinkPhoto(fileName!!, photoFile!!)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.generateLinkPhoto.collect { value ->
                when (value) {
                    is NetworkState.Init, is NetworkState.Loading -> {
                        binding.loading.visibility = View.VISIBLE
                        binding.send.visibility = View.INVISIBLE
                    }

                    is NetworkState.Success<*> -> {
                        binding.loading.visibility = View.INVISIBLE
                        val post = Post(
                            "",
                            content = binding.caption.text.toString(),
                            imageUrl = value.data.toString(),
                            Date(),
                            "",
                            mutableListOf()
                        )

                        uploadPhoto(post)
                        this.cancel()
                    }

                    else -> {
                        Toast.makeText(context, value.toString(), Toast.LENGTH_SHORT).show()
                        Log.d("Camera", value.toString())
                    }
                }
            }
        }
    }

    private fun uploadPhoto(post: Post) {
        viewModel.uploadPhoto(post)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uploadPhotoState.collect { value ->
                when (value) {
                    is NetworkState.Init, is NetworkState.Loading -> {
                        binding.loading.visibility = View.VISIBLE
                        binding.send.visibility = View.INVISIBLE
                    }

                    is NetworkState.Success<*> -> {
                        binding.loading.visibility = View.INVISIBLE
                        updateUI(false)
                    }

                    else -> {
                        Log.d("Camera", value.toString())
                    }
                }
            }
        }
    }

    private fun flipBitmapHorizontally(bitmap: Bitmap): Bitmap {
        val matrix = android.graphics.Matrix().apply {
            preScale(-1f, 1f) // lật ngang
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}