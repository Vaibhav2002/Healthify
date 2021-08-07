package com.vaibhav.healthify.ui.userDetailsInput.ageAndWeight

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.vaibhav.healthify.R
import com.vaibhav.healthify.databinding.FragmentUserAgeAndWeightBinding
import com.vaibhav.healthify.ui.MainActivity
import com.vaibhav.healthify.util.showToast
import com.vaibhav.healthify.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class UserAgeAndWeightFragment : Fragment(R.layout.fragment_user_age_and_weight) {

    private val binding by viewBinding(FragmentUserAgeAndWeightBinding::bind)
    private val viewModel: UserAgeAndWightViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.weightInput.doOnTextChanged { text, _, _, _ ->
            viewModel.onWeightChange(text.toString())
        }
        binding.ageInput.doOnTextChanged { text, _, _, _ ->
            viewModel.onAgeChange(text.toString())
        }
        binding.continueBtn.setOnClickListener {
            viewModel.onContinueButtonPressed()
        }

        collectUiState()
        collectEvents()
    }

    private fun collectEvents() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.events.collect {
                when (it) {
                    UserAgeAndWeightScreenEvents.NavigateToNextScreen -> {
                        Intent(requireContext(), MainActivity::class.java).also {
                            startActivity(it)
                            requireActivity().finish()
                        }
                    }
                    is UserAgeAndWeightScreenEvents.ShowToast -> requireContext().showToast(it.message)
                }
            }
        }
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                binding.continueBtn.isEnabled = it.isButtonEnabled

//                TODO("Add loading animation")
            }
        }
    }
}
