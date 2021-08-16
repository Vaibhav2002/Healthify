package com.vaibhav.healthify.ui.homeScreen.statsScreen.sleepStats

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.vaibhav.healthify.R
import com.vaibhav.healthify.databinding.FragmentSleepStatsBinding
import com.vaibhav.healthify.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class SleepStatsFragment : Fragment(R.layout.fragment_sleep_stats) {

    companion object {
        fun newInstance() = SleepStatsFragment()
    }

    private val viewModel by viewModels<SleepStatsViewModel>()
    private val binding by viewBinding(FragmentSleepStatsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureBarChart()
        configureLineChart()
        collectUiState()
    }

    private fun collectUiState() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.uiState.collect {
            Timber.d(it.toString())
            binding.percentage.text = "${it.weeklyPercentage}%"
            binding.exp.text = it.expGained.toString()
            binding.barChart.animate(it.barChartData)
            binding.lineChart.animate(it.lineChartData)
            binding.weekDate.text = it.weekDate
        }
    }

    private fun configureLineChart() {
        binding.lineChart.apply {
            animation.duration = 1000L
            labelsFormatter = {
                "${it.toInt()}%"
            }
            gradientFillColors =
                intArrayOf(
                    resources.getColor(R.color.graphColor),
                    Color.TRANSPARENT
                )
            labelsFont = ResourcesCompat.getFont(requireContext(), R.font.raleway_medium)
            labelsColor = resources.getColor(R.color.graphLabelColor)
            labelsSize =
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12F, resources.displayMetrics)
        }
    }

    private fun configureBarChart() {
        binding.barChart.apply {
            animation.duration = 1000L
            labelsFormatter = {
                val value = it.toInt()
                "$value"
            }
            labelsFont = ResourcesCompat.getFont(requireContext(), R.font.raleway_medium)
            labelsColor = resources.getColor(R.color.graphLabelColor)
            labelsSize =
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12F, resources.displayMetrics)
        }
    }
}
