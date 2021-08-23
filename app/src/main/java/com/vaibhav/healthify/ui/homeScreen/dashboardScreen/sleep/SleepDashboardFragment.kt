package com.vaibhav.healthify.ui.homeScreen.dashboardScreen.sleep

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.vaibhav.healthify.R
import com.vaibhav.healthify.databinding.FragmentSleepDashboardBinding
import com.vaibhav.healthify.ui.adapters.SleepLogAdapter
import com.vaibhav.healthify.ui.dialogs.addSleepDialog.AddSleepDialogFragment
import com.vaibhav.healthify.ui.dialogs.noInternetDialog.NoInternetDialogFragment
import com.vaibhav.healthify.util.OPEN_ADD_SLEEP_DIALOG
import com.vaibhav.healthify.util.OPEN_NO_INTERNET_DIALOG
import com.vaibhav.healthify.util.showToast
import com.vaibhav.healthify.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class SleepDashboardFragment : Fragment(R.layout.fragment_sleep_dashboard) {

    private val binding by viewBinding(FragmentSleepDashboardBinding::bind)
    private val viewModel: SleepDashboardViewModel by viewModels()
    private val sleepLogAdapter = SleepLogAdapter()

    companion object {
        fun newInstance() = SleepDashboardFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addSleep.setOnClickListener {
            viewModel.onAddSleepPressed()
        }
        binding.sleepLogRv.apply {
            setHasFixedSize(false)
            adapter = sleepLogAdapter
        }
        collectUiState()
        collectUiEvents()
    }

    private fun collectUiEvents() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.events.collect {
            when (it) {
                SleepDashboardScreenEvents.OpenAddSleepDialog -> openAddSleepDialog()
                is SleepDashboardScreenEvents.ShowToast -> requireContext().showToast(it.message)
                SleepDashboardScreenEvents.ShowNoInternetDialog -> showNoInternetDialog()
            }
        }
    }

    private fun collectUiState() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        viewModel.uiState.collect {
            binding.apply {
                Timber.d(it.toString())
                greetingText.text = it.greeting
                fotdText.text = it.factOfTheDay
                completedText.text = it.completedAmount.toString()
                totalText.text = "/ ${it.totalAmount} hrs"
                progress.progress = it.progress
                addSleep.isEnabled = it.isAddSleepButtonEnabled
                loadingLayout.loadingLayout.isVisible = it.isLoading
                sleepLogAdapter.submitList(it.sleepLog)
                yayText.text = it.mainGreeting
            }
        }
    }

    private fun openAddSleepDialog() {
        AddSleepDialogFragment(
            onTimeSelected = {
                viewModel.onSleepSelected(it)
            },
            onDismiss = {
                viewModel.onDialogClosed()
            }
        ).show(parentFragmentManager, OPEN_ADD_SLEEP_DIALOG)
    }

    private fun showNoInternetDialog() {
        NoInternetDialogFragment().show(parentFragmentManager, OPEN_NO_INTERNET_DIALOG)
    }
}
