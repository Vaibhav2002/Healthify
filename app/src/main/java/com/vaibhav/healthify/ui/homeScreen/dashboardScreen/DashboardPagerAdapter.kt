package com.vaibhav.healthify.ui.homeScreen.dashboardScreen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vaibhav.healthify.ui.homeScreen.dashboardScreen.sleep.SleepDashboardFragment
import com.vaibhav.healthify.ui.homeScreen.dashboardScreen.water.WaterDashboardFragment

class DashboardPagerAdapter(
    fragmentActivity: FragmentActivity,
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> WaterDashboardFragment.newInstance()
            else -> SleepDashboardFragment.newInstance()
        }
    }
}
