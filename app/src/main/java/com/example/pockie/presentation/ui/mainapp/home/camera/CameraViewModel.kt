package com.example.pockie.presentation.ui.mainapp.home.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockie.domain.model.Post
import com.example.pockie.domain.usecase.GenerateLinkPhotoUseCase
import com.example.pockie.domain.usecase.GetCurrentUserIdUseCase
import com.example.pockie.domain.usecase.UploadPhotoUseCase
import com.example.pockie.presentation.utils.networkstate.NetworkState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.upload
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val generateLinkPhotoUseCase: GenerateLinkPhotoUseCase,
    private val uploadPhotoUseCase: UploadPhotoUseCase,
    private val firebaseMessaging: FirebaseMessaging,
    private val firebaseFirestore: FirebaseFirestore,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
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

    fun getFCMToken(){
        firebaseMessaging.token.addOnCompleteListener{
            if(it.isSuccessful){
                val fcmToken = it.result
                viewModelScope.launch {
                    getCurrentUserIdUseCase()?.let { it1 ->
                        val currentFCMToken = firebaseFirestore.collection("accounts").document(it1)
                            .get().await().getString("fcmToken")
                        if(fcmToken != currentFCMToken) {
                            firebaseFirestore.collection("accounts").document(it1)
                                .update("fcmToken", fcmToken)
                        }
                    }
                }
            }
        }
    }
}