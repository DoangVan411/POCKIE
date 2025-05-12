package com.example.pockie.presentation.ui.mainapp.home.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockie.domain.model.Post
import com.example.pockie.domain.usecase.GenerateLinkPhotoUseCase
import com.example.pockie.domain.usecase.UploadPhotoUseCase
import com.example.pockie.presentation.utils.networkstate.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.upload
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val generateLinkPhotoUseCase: GenerateLinkPhotoUseCase,
    private val uploadPhotoUseCase: UploadPhotoUseCase,
): ViewModel() {
    private val _generateLinkPhoto = MutableStateFlow<NetworkState>(NetworkState.Init)
    val generateLinkPhoto: StateFlow<NetworkState> get() = _generateLinkPhoto

    private val _uploadPhotoState = MutableStateFlow<NetworkState>(NetworkState.Init)
    val uploadPhotoState: StateFlow<NetworkState> get() = _uploadPhotoState

    fun generateLinkPhoto(fileName: String, photoFile: File){
        _generateLinkPhoto.value = NetworkState.Loading
        viewModelScope.launch {
            generateLinkPhotoUseCase.invoke(fileName, photoFile).collect{_generateLinkPhoto.value = it}
        }
    }

    fun uploadPhoto(post: Post){
        _uploadPhotoState.value = NetworkState.Loading
        viewModelScope.launch {
            uploadPhotoUseCase.invoke(post).collect{_uploadPhotoState.value = it}
        }
    }
}