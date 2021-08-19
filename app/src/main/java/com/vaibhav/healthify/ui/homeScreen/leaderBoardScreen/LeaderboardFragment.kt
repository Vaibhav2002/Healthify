package com.vaibhav.healthify.ui.homeScreen.leaderBoardScreen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.vaibhav.healthify.R
import com.vaibhav.healthify.databinding.FragmentLeaderboardBinding
import com.vaibhav.healthify.ui.adapters.LeaderboardAdapter
import com.vaibhav.healthify.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LeaderboardFragment : Fragment(R.layout.fragment_leaderboard) {

    private val binding by viewBinding(FragmentLeaderboardBinding::bind)
    private val viewModel by viewModels<LeaderboardViewModel>()
    private val leaderboardAdapter = LeaderboardAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        collectUiState()
    }

    private fun collectUiState() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.uiState.collect {
            leaderboardAdapter.submitList(it.leaderboardItems)
        }
    }

    private fun initRecyclerView() {
        binding.leaderboardRv.apply {
            adapter = leaderboardAdapter
            setHasFixedSize(false)
        }
    }
}
