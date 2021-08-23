package com.vaibhav.healthify.ui.homeScreen.dashboardScreen.water

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.vaibhav.healthify.R
import com.vaibhav.healthify.databinding.FragmentWaterDashboardBinding
import com.vaibhav.healthify.ui.adapters.WaterLogAdapter
import com.vaibhav.healthify.ui.dialogs.addWaterDialog.AddWaterDialogFragment
import com.vaibhav.healthify.ui.dialogs.noInternetDialog.NoInternetDialogFragment
import com.vaibhav.healthify.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class WaterDashboardFragment : Fragment(R.layout.fragment_water_dashboard) {

    private val binding by viewBinding(FragmentWaterDashboardBinding::bind)
    private val viewModel: WaterDashboardViewModel by viewModels()
    private val waterAdapter = WaterLogAdapter()

    companion object {
        fun newInstance() = WaterDashboardFragment()
        const val ALARM_CODE = 15
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addWater.setOnClickListener {
            viewModel.onAddWaterPressed()
        }
        binding.drinkLogRv.apply {
            setHasFixedSize(false)
            adapter = waterAdapter
        }
        collectUiState()
        collectUiEvents()
    }

    private fun collectUiEvents() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.events.collect {
            when (it) {
                WaterDashboardScreenEvents.OpenAddWaterDialog -> openAddWaterDialog()
                is WaterDashboardScreenEvents.ShowToast -> requireContext().showToast(it.message)
                WaterDashboardScreenEvents.CreateAlarm -> createAlarm()
                WaterDashboardScreenEvents.ShowNoInternetDialog -> showNoInternetDialog()
            }
        }
    }

    private fun collectUiState() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        viewModel.uiState.collect {
            binding.apply {
                greetingText.text = it.greeting
                fotdText.text = it.factOfTheDay
                completedText.text = it.completedAmount.toString()
                totalText.text = "/ ${it.totalAmount} mL"
                progress.progress = it.progress
                addWater.isEnabled = it.isAddWaterButtonEnabled
                loadingLayout.loadingLayout.isVisible = it.isLoading
                waterAdapter.submitList(it.waterLog)
                yayText.text = it.mainGreeting
            }
        }
    }

    private fun openAddWaterDialog() {
        AddWaterDialogFragment(onAmountSelected = {
            viewModel.onWaterSelected(it)
        }, onDismiss = {
            viewModel.onDialogClosed()
        }).show(parentFragmentManager, OPEN_ADD_WATER_DIALOG)
    }

    private fun showNoInternetDialog() {
        NoInternetDialogFragment().show(parentFragmentManager, OPEN_NO_INTERNET_DIALOG)
    }

    private fun createAlarm() {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), WaterBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            ALARM_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + NOTIFICATION_INTERVAL,
            pendingIntent
        )
    }
}
