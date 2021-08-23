package com.vaibhav.healthify.ui.userDetailsInput.userName

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.vaibhav.healthify.R
import com.vaibhav.healthify.databinding.FragmentGetUserNameBinding
import com.vaibhav.healthify.ui.dialogs.noInternetDialog.NoInternetDialogFragment
import com.vaibhav.healthify.util.OPEN_NO_INTERNET_DIALOG
import com.vaibhav.healthify.util.showToast
import com.vaibhav.healthify.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class GetUserNameFragment : Fragment(R.layout.fragment_get_user_name) {

    private val binding by viewBinding(FragmentGetUserNameBinding::bind)
    private val viewModel: GetUserNameViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("In user input")
        binding.usernameInput.doOnTextChanged { text, _, _, _ ->
            viewModel.onUserNameChange(text.toString())
        }
        binding.nextButton.setOnClickListener {
            viewModel.onNextButtonClicked()
        }

        collectUiState()
        collectUiEvents()
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                binding.nextButton.isEnabled = it.isNextButtonEnabled
                binding.loadingLayout.loadingLayout.isVisible = it.isLoading
            }
        }
    }

    private fun collectUiEvents() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.events.collect {
                when (it) {
                    GetUserNameScreenEvents.NavigateToNextScreen -> {
                        findNavController().navigate(R.id.action_getUserNameFragment_to_userWeightFragment)
                    }
                    is GetUserNameScreenEvents.ShowToast -> requireContext().showToast(it.message)
                    GetUserNameScreenEvents.ShowNoInternetDialog -> openNoInternetDialog()
                }
            }
        }
    }

    private fun openNoInternetDialog() {
        NoInternetDialogFragment().show(parentFragmentManager, OPEN_NO_INTERNET_DIALOG)
    }
}
