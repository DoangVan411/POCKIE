package com.example.pockie.presentation.ui.mainapp.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): ViewModel() {

    fun LogOut(){
        firebaseAuth.signOut()
        Log.d("Login", firebaseAuth.currentUser?.uid ?: "null")
    }
}