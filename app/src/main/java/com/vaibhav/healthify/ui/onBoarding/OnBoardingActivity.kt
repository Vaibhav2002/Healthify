package com.vaibhav.healthify.ui.onBoarding

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.vaibhav.chatofy.util.makeStatusBarTransparent
import com.vaibhav.healthify.R
import com.vaibhav.healthify.databinding.ActivityOnBoardingBinding
import com.vaibhav.healthify.ui.adapters.OnboardingAdapter
import com.vaibhav.healthify.ui.auth.AuthActivity
import com.vaibhav.healthify.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class OnBoardingActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityOnBoardingBinding::inflate)
    private val viewModel by viewModels<OnBoardingViewModel>()
    private lateinit var onBoardingAdapter: OnboardingAdapter
    private lateinit var animation: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        makeStatusBarTransparent()
        onBoardingAdapter = OnboardingAdapter(viewModel.onboardingList)
        animation = AnimationUtils.loadAnimation(this, R.anim.scale_from_center)
        setupViewPager()
        collectUiState()
        collectUiEvents()
        binding.skipButton.setOnClickListener {
            viewModel.onSKipButtonPressed()
        }
        binding.nextButton.setOnClickListener {
            viewModel.onNextButtonPressed()
        }
        binding.dotsIndicator.dotsClickable = false
    }

    private fun collectUiState() = lifecycleScope.launchWhenStarted {
        viewModel.uiState.collect {
            binding.title.text = it.title
            binding.title.startAnimation(animation)
            binding.description.text = it.subtitle
            binding.description.startAnimation(animation)
            binding.skipButton.isVisible = it.isSkipButtonVisible
        }
    }

    private fun collectUiEvents() = lifecycleScope.launchWhenStarted {
        viewModel.events.collect {
            when (it) {
                OnBoardingScreenEvents.NavigateToLoginScreen -> navigateToLoginScreen()
                is OnBoardingScreenEvents.ShowNextPage -> showNextPage(it.pageNo)
            }
        }
    }

    private fun setupViewPager() {
        binding.viewPager.apply {
            isUserInputEnabled = false
            adapter = onBoardingAdapter
        }
        val viewPager2 = findViewById<ViewPager2>(R.id.viewPager)
        binding.dotsIndicator.setViewPager2(viewPager2)
    }

    private fun showNextPage(pageNo: Int) {
        binding.viewPager.setCurrentItem(pageNo, true)
    }

    private fun navigateToLoginScreen() {
        Intent(this, AuthActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }
}
