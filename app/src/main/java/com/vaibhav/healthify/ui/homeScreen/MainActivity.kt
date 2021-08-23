package com.vaibhav.healthify.ui.homeScreen

import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.vaibhav.healthify.R
import com.vaibhav.healthify.databinding.ActivityMainBinding
import com.vaibhav.healthify.ui.dialogs.noInternetDialog.NoInternetDialogFragment
import com.vaibhav.healthify.util.OPEN_NO_INTERNET_DIALOG
import com.vaibhav.healthify.util.showToast
import com.vaibhav.healthify.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)
    private val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var vibrator: Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        navController = findNavController(R.id.fragmentContainerView3)
        binding.bottomNav.setupWithNavController(navController)
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        collectUiEvents()
        collectLoadingState()
        binding.bottomNav.setOnItemSelectedListener {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    100L, VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
            NavigationUI.onNavDestinationSelected(it, navController)
        }
    }

    private fun collectLoadingState() {
        lifecycleScope.launchWhenStarted {
            viewModel.isLoading.collect {
                binding.loadingLayout.loadingLayout.isVisible = it
            }
        }
    }

    private fun collectUiEvents() {
        lifecycleScope.launchWhenStarted {
            viewModel.events.collectLatest {
                when (it) {
                    is MainActivityScreenEvents.ShowToast -> showToast(it.message)
                    MainActivityScreenEvents.ShowNoInternetDialog -> showNoInternetDialog()
                }
            }
        }
    }

    private fun showNoInternetDialog() {
        NoInternetDialogFragment().show(supportFragmentManager, OPEN_NO_INTERNET_DIALOG)
    }
}
