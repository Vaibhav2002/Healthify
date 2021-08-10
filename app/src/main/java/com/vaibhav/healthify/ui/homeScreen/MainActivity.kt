package com.vaibhav.healthify.ui.homeScreen

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.vaibhav.healthify.R
import com.vaibhav.healthify.databinding.ActivityMainBinding
import com.vaibhav.healthify.util.showToast
import com.vaibhav.healthify.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)
    private val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        navController = findNavController(R.id.fragmentContainerView3)
        binding.bottomNav.setupWithNavController(navController)

        collectUiEvents()
        collectLoadingState()
    }

    private fun collectLoadingState() {
        lifecycleScope.launchWhenStarted {
            viewModel.isLoading.collect {
                binding.loadingLayout.loadingAnim.isVisible = it
            }
        }
    }

    private fun collectUiEvents() {
        lifecycleScope.launchWhenStarted {
            viewModel.events.collect {
                when (it) {
                    is MainActivityScreenEvents.ShowToast -> showToast(it.message)
                }
            }
        }
    }
}
