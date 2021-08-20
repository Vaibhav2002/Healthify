package com.vaibhav.healthify.ui.homeScreen.statsScreen.sleepStats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.healthify.data.models.local.Sleep
import com.vaibhav.healthify.data.repo.AuthRepo
import com.vaibhav.healthify.data.repo.SleepRepo
import com.vaibhav.healthify.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SleepStatsViewModel @Inject constructor(
    private val authRepo: AuthRepo,
    private val sleepRepo: SleepRepo,
    private val sleepChartOrganizer: ChartDataOrganizer<Sleep>
) : ViewModel() {

    private val _uiState = MutableStateFlow(SleepStatsScreenState())
    val uiState: StateFlow<SleepStatsScreenState> = _uiState

    private val user =
        authRepo.getUserDataFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    init {
        collectUserData()
        viewModelScope.launch {
            sleepChartOrganizer.setUp()
            collectSleepLogs()
        }
    }

    private fun collectUserData() = viewModelScope.launch {
        user.collect { user ->
        }
    }

    private fun collectSleepLogs() = viewModelScope.launch {
        sleepRepo.getAllSleepsOfLastWeek().collect { logs ->
            logs.forEach {
                val index = sleepChartOrganizer.findCalendarInstance(it.timeStamp)
                sleepChartOrganizer.add(it, index)
            }
            sleepChartOrganizer.sortData()
            calculateExp(logs)
            prepareDataForCharts(logs)
        }
    }

    private fun calculateExp(logs: List<Sleep>) = viewModelScope.launch {
        val exp = logs.size * WATER_EXP
        _uiState.emit(uiState.value.copy(expGained = exp.toLong()))
    }

    private fun calculatePercentage(logs: List<Sleep>, days: Int) = viewModelScope.launch {
        user.value?.let {
            val percentage = if (days != 0) {
                val totalSlept = logs.sumOf { sleep ->
                    sleep.sleepDuration
                }
                ((totalSlept.toFloat() / (it.waterLimit * days)) * 100f).roundOff()
            } else 0f
            _uiState.emit(uiState.value.copy(weeklyPercentage = percentage))
        }
    }

    private suspend fun prepareDataForCharts(logs: List<Sleep>) = viewModelScope.launch {
        val barList = mutableListOf<Pair<String, Float>>()
        val lineList = mutableListOf<Pair<String, Float>>()
        var startDate = ""
        var endDate = ""
        var numberOfDaysSlept = 0
        var index = 0
        val length = sleepChartOrganizer.data.size
        sleepChartOrganizer.data.forEach {
            index++
            val day = DAYS.getDayFromNumber(it.key[Calendar.DAY_OF_WEEK])
            val totalSlept = it.value.sumOf { sleep ->
                sleep.sleepDuration
            }
            if (index == 1)
                startDate = it.key.getFormattedDate()
            else if (index == length)
                endDate = it.key.getFormattedDate()
            var dailyPercentage = (totalSlept.toFloat() / user.value!!.sleepLimit) * 100f
            dailyPercentage = dailyPercentage.roundOff()
            if (it.value.size > 0)
                numberOfDaysSlept++
            val pair = Pair(day.getShortFormFromNumber(), totalSlept.getHoursFromMinutes())
            val lineChartPair = Pair(day.getShortFormFromNumber(), dailyPercentage)
            barList.add(pair)
            lineList.add(lineChartPair)
        }
        lineList.reverse()
        val weekDate = "$endDate - $startDate"
        calculatePercentage(logs, numberOfDaysSlept)
        _uiState.emit(
            uiState.value.copy(
                barChartData = barList,
                lineChartData = lineList,
                weekDate = weekDate
            )
        )
    }
}
