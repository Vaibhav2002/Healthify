package com.vaibhav.healthify.ui.splashScreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.vaibhav.chatofy.util.makeStatusBarTransparent
import com.vaibhav.healthify.R
import com.vaibhav.healthify.databinding.ActivitySplashBinding
import com.vaibhav.healthify.ui.auth.AuthActivity
import com.vaibhav.healthify.ui.homeScreen.MainActivity
import com.vaibhav.healthify.ui.onBoarding.OnBoardingActivity
import com.vaibhav.healthify.ui.splashScreen.SplashScreenEvents.*
import com.vaibhav.healthify.ui.userDetailsInput.UserDetailsActivity
import com.vaibhav.healthify.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    companion object {
        private const val SPLASH_SCREEN_DURATION = 2000L
    }

    private val viewModel by viewModels<SplashViewModel>()
    private val binding by viewBinding(ActivitySplashBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        makeStatusBarTransparent()
        collectUiEvents()
        animateLogo()
        Handler().postDelayed({
            viewModel.onTimerComplete()
        }, SPLASH_SCREEN_DURATION)
    }

    private fun animateLogo() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.scale_logo)
        binding.logo.apply {
            startAnimation(animation)
        }
    }

    private fun collectUiEvents() = lifecycleScope.launchWhenStarted {
        viewModel.events.collect {
            when (it) {
                NavigateToHomeScreen -> navigateToHomeScreen()
                NavigateToLoginScreen -> navigateToLoginScreen()
                NavigateToOnBoarding -> navigateToOnBoardingScreen()
                NavigateToUserDetailsScreen -> navigateToUserDetailsScreen()
            }
        }
    }

    private fun navigateToHomeScreen() {
        Intent(this, MainActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }

    private fun navigateToLoginScreen() {
        Intent(this, AuthActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }

    private fun navigateToOnBoardingScreen() {
        Intent(this, OnBoardingActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }

    private fun navigateToUserDetailsScreen() {
        Intent(this, UserDetailsActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }
}
