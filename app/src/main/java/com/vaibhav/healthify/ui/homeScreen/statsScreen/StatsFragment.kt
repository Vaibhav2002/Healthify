package com.vaibhav.healthify.ui.homeScreen.statsScreen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.vaibhav.healthify.R
import com.vaibhav.healthify.databinding.FragmentStatsBinding
import com.vaibhav.healthify.util.setMarginTopForFullScreen
import com.vaibhav.healthify.util.viewBinding

class StatsFragment : Fragment(R.layout.fragment_stats) {

    private val binding by viewBinding(FragmentStatsBinding::bind)
    private lateinit var statsViewPagerAdapter: StatsPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.headerText.setMarginTopForFullScreen()
        statsViewPagerAdapter = StatsPagerAdapter(requireActivity())
        initViewPager()
    }

    private fun initViewPager() {
        binding.viewpager.adapter = statsViewPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
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
