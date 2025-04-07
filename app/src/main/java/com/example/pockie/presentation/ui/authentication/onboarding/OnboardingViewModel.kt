package com.example.pockie.presentation.ui.authentication.onboarding

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): ViewModel(){
    fun checkLogin(): Boolean{
        val uid = firebaseAuth.currentUser?.uid
        Log.d("Login", uid.toString())
        return !uid.isNullOrEmpty()
    }
}