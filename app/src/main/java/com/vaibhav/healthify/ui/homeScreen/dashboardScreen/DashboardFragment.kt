package com.vaibhav.healthify.ui.homeScreen.dashboardScreen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.vaibhav.healthify.R
import com.vaibhav.healthify.databinding.FragmentDashboardBinding
import com.vaibhav.healthify.util.setMarginTopForFullScreen
import com.vaibhav.healthify.util.viewBinding

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private lateinit var dashboardPagerAdapter: DashboardPagerAdapter
    private val binding by viewBinding(FragmentDashboardBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.headerText.setMarginTopForFullScreen()
        dashboardPagerAdapter = DashboardPagerAdapter(requireActivity())
        initViewPager()
    }

    private fun initViewPager() {
        binding.viewpager.adapter = dashboardPagerAdapter
        binding.viewpager.isSaveEnabled = false
        TabLayoutMediator(binding.tableLayout, binding.viewpager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Water"
                }
                1 -> {
                    tab.text = "Sleep"
                }
            }
        }.attach()
    }
}
