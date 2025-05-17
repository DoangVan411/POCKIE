package com.example.pockie.presentation.ui.mainapp.profile.edit_profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pockie.domain.usecase.EditAccountUseCase
import com.example.pockie.domain.usecase.GenerateAvtLinkPhotoUseCase
import com.example.pockie.domain.usecase.GenerateLinkPhotoUseCase
import com.example.pockie.presentation.utils.networkstate.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val editAccountUseCase: EditAccountUseCase,
    private val generateAvtLinkPhotoUseCase: GenerateAvtLinkPhotoUseCase
): ViewModel() {
    private val _generateAvtLinkPhoto = MutableStateFlow<NetworkState>(NetworkState.Init)
    val generateAvtLinkPhoto: StateFlow<NetworkState> get() = _generateAvtLinkPhoto

    private val _edit =  MutableStateFlow<NetworkState>(NetworkState.Init)
    val edit: StateFlow<NetworkState> get() = _edit

    fun generateAvtLinkPhoto(fileName: String, uri: Uri, context: Context){
        _generateAvtLinkPhoto.value = NetworkState.Loading
        viewModelScope.launch {
            generateAvtLinkPhotoUseCase.invoke(fileName, uri, context).collect{_generateAvtLinkPhoto.value = it}
        }
    }

    fun editProfile(fullName: String, bio: String, avtUrl: String){
        _edit.value = NetworkState.Loading
        viewModelScope.launch {
            editAccountUseCase.invoke(fullName, bio, avtUrl).collect{_edit.value = it}
        }
    }
}