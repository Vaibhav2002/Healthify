package com.vaibhav.healthify.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class ChartDataOrganizer<T> {

    private val days = mutableListOf<Calendar>()
    val millisInADay = 24 * 60 * 60 * 1000
    val data: MutableMap<Calendar, MutableList<T>> = mutableMapOf()

    suspend fun setUp() {
        initializeAllDays()
        initDays()
    }

    private suspend fun initializeAllDays() = withContext(Dispatchers.IO) {
        for (i in 1..7)
            days.add(Calendar.getInstance())
        days[0].set(Calendar.HOUR_OF_DAY, 0)
        for (i in 1 until days.size)
            days[i].timeInMillis = days[i - 1].timeInMillis - millisInADay
    }

    private suspend fun initDays() =
        withContext(Dispatchers.IO) {
            val iterator = days.iterator()
            while (iterator.hasNext()) {
                data[iterator.next()] = mutableListOf()
            }
        }

    suspend fun add(data: T, index: Calendar?) {
        index?.let {
            this.data[it]?.add(data)
        }
    }

    suspend fun sortData() = withContext(Dispatchers.IO) {
        data.toSortedMap { cal1, cal2 -> cal2.timeInMillis.compareTo(cal1.timeInMillis) }
    }

    suspend fun findCalendarInstance(timeStamp: Long): Calendar? =
        withContext(Dispatchers.IO) {
            var cal: Calendar? = null
            for (calendar in days) {
                if (calendar.timeInMillis < timeStamp) {
                    cal = calendar
                    break
                }
            }
            return@withContext cal
        }
}
