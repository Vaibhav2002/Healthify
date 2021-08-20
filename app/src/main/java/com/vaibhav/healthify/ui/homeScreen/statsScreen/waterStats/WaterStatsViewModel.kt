package com.vaibhav.healthify.ui.homeScreen.statsScreen.waterStats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.healthify.data.models.local.Water
import com.vaibhav.healthify.data.repo.AuthRepo
import com.vaibhav.healthify.data.repo.WaterRepo
import com.vaibhav.healthify.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class WaterStatsViewModel @Inject constructor(
    private val authRepo: AuthRepo,
    private val waterRepo: WaterRepo,
    private val waterChartDataOrganizer: ChartDataOrganizer<Water>
) : ViewModel() {

    private val _uiState = MutableStateFlow(WaterStatsScreenState())
    val uiState: StateFlow<WaterStatsScreenState> = _uiState

    private val user =
        authRepo.getUserDataFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    init {
        collectUserData()
        viewModelScope.launch {
            waterChartDataOrganizer.setUp()
            collectWaterLogs()
        }
    }

    private fun collectUserData() = viewModelScope.launch {
        user.collect { user ->
        }
    }

    private fun collectWaterLogs() = viewModelScope.launch {
        waterRepo.getAllWaterLogsOfLastWeek().collect { logs ->
            logs.forEach {
                val index = waterChartDataOrganizer.findCalendarInstance(it.timeStamp)
                waterChartDataOrganizer.add(it, index)
            }
            waterChartDataOrganizer.sortData()
            calculateExp(logs)
            prepareDataForCharts(logs)
        }
    }

    private fun calculateExp(logs: List<Water>) = viewModelScope.launch {
        val exp = logs.size * WATER_EXP
        _uiState.emit(uiState.value.copy(expGained = exp.toLong()))
    }

    private fun calculatePercentage(logs: List<Water>, days: Int) = viewModelScope.launch {
        user.value?.let {
            val percentage = if (days != 0) {
                val totalAmountDrank = logs.sumOf { water ->
                    water.quantity.quantity
                }
                ((totalAmountDrank.toFloat() / (it.waterLimit * days)) * 100f).roundOff()
            } else 0f
            _uiState.emit(uiState.value.copy(weeklyPercentage = percentage))
        }
    }

    private suspend fun prepareDataForCharts(logs: List<Water>) = viewModelScope.launch {
        val barList = mutableListOf<Pair<String, Float>>()
        val lineList = mutableListOf<Pair<String, Float>>()
        var startDate = ""
        var endDate = ""
        var numberOfDaysDrank = 0
        var index = 0
        val length = waterChartDataOrganizer.data.size
        waterChartDataOrganizer.data.forEach {
            index++
            val day = DAYS.getDayFromNumber(it.key[Calendar.DAY_OF_WEEK])
            val totalAmountDrank = it.value.sumOf { water ->
                water.quantity.quantity
            }
            if (index == 1)
                startDate = it.key.getFormattedDate()
            else if (index == length)
                endDate = it.key.getFormattedDate()
            var dailyPercentage = (totalAmountDrank.toFloat() / user.value!!.waterLimit) * 100f
            dailyPercentage = dailyPercentage.roundOff()
            if (it.value.size > 0)
                numberOfDaysDrank++
            val pair = Pair(day.getShortFormFromNumber(), totalAmountDrank.toFloat())
            val lineChartPair = Pair(day.getShortFormFromNumber(), dailyPercentage)
            barList.add(pair)
            lineList.add(lineChartPair)
        }
        lineList.reverse()
        val weekDate = "$endDate - $startDate"
        calculatePercentage(logs, numberOfDaysDrank)
        _uiState.emit(
            uiState.value.copy(
                barChartData = barList,
                lineChartData = lineList,
                weekDate = weekDate
            )
        )
    }
}
