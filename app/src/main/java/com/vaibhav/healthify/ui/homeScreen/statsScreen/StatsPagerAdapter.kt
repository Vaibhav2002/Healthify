package com.vaibhav.healthify.ui.homeScreen.statsScreen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vaibhav.healthify.ui.homeScreen.statsScreen.sleepStats.SleepStatsFragment
import com.vaibhav.healthify.ui.homeScreen.statsScreen.waterStats.WaterStatsFragment

class StatsPagerAdapter(
    fragmentActivity: FragmentActivity,
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> WaterStatsFragment.newInstance()
            else -> SleepStatsFragment.newInstance()
        }
    }
}
