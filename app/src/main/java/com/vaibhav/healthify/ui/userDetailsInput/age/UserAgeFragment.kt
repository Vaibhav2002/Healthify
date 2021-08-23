package com.vaibhav.healthify.ui.userDetailsInput.age

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.vaibhav.healthify.R
import com.vaibhav.healthify.databinding.FragmentUserAgeBinding
import com.vaibhav.healthify.ui.dialogs.noInternetDialog.NoInternetDialogFragment
import com.vaibhav.healthify.ui.homeScreen.MainActivity
import com.vaibhav.healthify.util.OPEN_NO_INTERNET_DIALOG
import com.vaibhav.healthify.util.setMarginTopForFullScreen
import com.vaibhav.healthify.util.showToast
import com.vaibhav.healthify.util.viewBinding
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class UserAgeFragment : Fragment(R.layout.fragment_user_age) {

    private val binding by viewBinding(FragmentUserAgeBinding::bind)
    private val viewModel by viewModels<UserAgeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.userAgeProgressBar.setProgress(viewModel.uiState.value.age.toFloat())
        binding.continueBtn.setOnClickListener {
            viewModel.onContinueButtonPressed()
        }
        binding.backButton.setMarginTopForFullScreen()
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.userAgeProgressBar.onSeekChangeListener = object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams?) {
                seekParams?.let { viewModel.onAgeChange(it.progress) }
            }

            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar?) = Unit

            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar?) = Unit
        }

        collectUiState()
        collectUIEvents()
    }

    private fun collectUIEvents() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                binding.userAge.text = "${it.age} yrs"
                binding.continueBtn.isEnabled = it.isButtonEnabled
                binding.loadingLayout.loadingLayout.isVisible = it.isLoading
            }
        }
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.events.collect {
                when (it) {
                    UserAgeScreenEvents.NavigateToHomeScreen -> navigateToHomeScreen()
                    is UserAgeScreenEvents.ShowToast -> requireContext().showToast(it.message)
                    UserAgeScreenEvents.ShowNoInternetDialog -> openNoInternetDialog()
                }
            }
        }
    }

    private fun navigateToHomeScreen() {
        Intent(requireContext(), MainActivity::class.java).also {
            startActivity(it)
            requireActivity().finish()
        }
    }

    private fun openNoInternetDialog() {
        NoInternetDialogFragment().show(parentFragmentManager, OPEN_NO_INTERNET_DIALOG)
    }
}
