package com.taskmanager.taskmanagement.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taskmanager.taskmanagement.domain.usecases.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
): ViewModel() {
    fun signOut(){
        viewModelScope.launch {
            signOutUseCase()
        }
    }
}