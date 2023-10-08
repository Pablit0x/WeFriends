package com.ps.wefriends.presentation.screens.home

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val auth: FirebaseAuth): ViewModel() {


    private val _isSignOutDialogOpen = MutableStateFlow(false)
    val isSignOutDialogOpen = _isSignOutDialogOpen.asStateFlow()

    fun openSignOutDialog(){
        _isSignOutDialogOpen.update { true }
    }

    fun closeSignOutDialog(){
        _isSignOutDialogOpen.update { false }
    }

    fun signOut(){
        auth.signOut()
    }

}