package com.vaibhav.healthify.ui.homeScreen.dashboardScreen

import androidx.lifecycle.ViewModel
import com.vaibhav.healthify.data.repo.AuthRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val authRepo: AuthRepo) : ViewModel() {

    val user = authRepo.getUserDataFlow()
}
