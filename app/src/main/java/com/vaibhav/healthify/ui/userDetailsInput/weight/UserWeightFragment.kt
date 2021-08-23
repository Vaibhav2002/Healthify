package com.vaibhav.healthify.ui.userDetailsInput.weight

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.syzible.scales.ScaleSliderAdapter
import com.syzible.scales.ScaleSliderLayoutManager
import com.syzible.scales.Screen
import com.vaibhav.healthify.R
import com.vaibhav.healthify.databinding.FragmentUserWeightBinding
import com.vaibhav.healthify.ui.dialogs.noInternetDialog.NoInternetDialogFragment
import com.vaibhav.healthify.util.OPEN_NO_INTERNET_DIALOG
import com.vaibhav.healthify.util.setMarginTopForFullScreen
import com.vaibhav.healthify.util.showToast
import com.vaibhav.healthify.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class UserWeightFragment :
    Fragment(R.layout.fragment_user_weight),
    ScaleSliderLayoutManager.MovementListener {

    private val binding by viewBinding(FragmentUserWeightBinding::bind)
    private val viewModel: UserWeightViewModel by viewModels()
    private val weightAdapter = ScaleSliderAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nextButton.setOnClickListener {
            viewModel.onNextButtonPressed()
        }
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.backButton.setMarginTopForFullScreen()
        setUpWeightScale()
        collectUiState()
        collectEvents()
    }

    private fun setUpWeightScale() {
        binding.weightRecyclerView.apply {
            val padding = Screen.getScreenWidth(requireContext()) / 2
            setPadding(padding, 0, padding, 0)
            adapter = weightAdapter
            itemAnimator = DefaultItemAnimator()
            layoutManager = ScaleSliderLayoutManager(requireContext(), this@UserWeightFragment)
        }
        weightAdapter.apply {
            setSpokeColour(resources.getColor(R.color.spokeColor))
            setCountBetweenMarkers(5)
            setData(viewModel.weightList)
        }
        binding.weightRecyclerView.scrollToPosition(viewModel.DEFAULT_WEIGHT)
    }

    private fun collectEvents() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.events.collect {
                when (it) {
                    UserWeightScreenEvents.NavigateToNextScreen -> {
                        findNavController().navigate(R.id.action_userWeightFragment_to_userAgeFragment)
                    }
                    is UserWeightScreenEvents.ShowToast -> requireContext().showToast(it.message)
                    UserWeightScreenEvents.ShowNoInternetDialog -> openNoInternetDialog()
                }
            }
        }
    }

    private fun openNoInternetDialog() {
        NoInternetDialogFragment().show(parentFragmentManager, OPEN_NO_INTERNET_DIALOG)
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                binding.nextButton.isEnabled = it.isButtonEnabled
                binding.userWeight.text = "${it.weight} Kg"
                binding.loadingLayout.loadingLayout.isVisible = it.isLoading
            }
        }
    }

    override fun onItemSelected(selectedIndex: Int) = Unit

    override fun onItemChanged(selectedIndex: Int) {
        viewModel.onWeightChange(selectedIndex)
    }
}
