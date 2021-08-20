package com.vaibhav.healthify.ui.homeScreen.profileScreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.vaibhav.healthify.R
import com.vaibhav.healthify.databinding.FragmentProfileBinding
import com.vaibhav.healthify.ui.auth.AuthActivity
import com.vaibhav.healthify.util.loadImageUrl
import com.vaibhav.healthify.util.showDialog
import com.vaibhav.healthify.util.showToast
import com.vaibhav.healthify.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val viewModel by viewModels<ProfileViewModel>()

    @Inject
    lateinit var auth0: Auth0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectUiState()
        collectUiEvents()

        binding.aboutButton.setOnClickListener {
            viewModel.onAboutPressed()
        }
        binding.logoutButton.setOnClickListener {
            viewModel.onLogoutPressed()
        }
        binding.rankCard.setOnClickListener {
            viewModel.onLeaderBoardClicked()
        }
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = true
            viewModel.onRefreshed()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun collectUiEvents() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.uiState.collect {
            binding.apply {
                username.text = it.username
                profileImage.loadImageUrl(it.profileImage)
                expTv.text = it.exp.toString()
                weightTv.text = "${it.weight} kgs"
                ageTv.text = it.age.toString()
                rankingTv.text = it.rank.toString()
                loadingLayout.loadingLayout.isVisible = it.isLoading
            }
        }
    }

    private fun collectUiState() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.events.collect {
            when (it) {
                ProfileScreenEvents.Logout -> logout()
                ProfileScreenEvents.NavigateToAboutScreen -> navigateToAboutScreen()
                ProfileScreenEvents.NavigateToAuthScreen -> navigateToAuthScreen()
                is ProfileScreenEvents.ShowLogoutDialog -> showLogoutDialog(
                    it.title,
                    it.description
                )
                is ProfileScreenEvents.ShowToast -> requireContext().showToast(it.message)
                ProfileScreenEvents.NavigateToLeaderBoardScreen -> navigateToLeaderboardScreen()
            }
        }
    }

    fun logout() {
        viewModel.disableLogoutButton()
        WebAuthProvider
            .logout(auth0)
            .withScheme(getString(R.string.scheme))
            .start(
                requireContext(),
                object : Callback<Void?, AuthenticationException> {
                    override fun onFailure(error: AuthenticationException) {
                        viewModel.onLogoutFailed(error)
                    }

                    override fun onSuccess(result: Void?) {
                        viewModel.onLogoutSuccess()
                    }
                }
            )
    }

    fun showLogoutDialog(title: String, message: String) {
        requireContext().showDialog(title, message, onConfirm = {
            viewModel.onLogoutConfirmed()
        }, onDismiss = {
            viewModel.onDialogClosed()
        })
    }

    fun navigateToAuthScreen() {
        Intent(requireContext(), AuthActivity::class.java).also {
            startActivity(it)
            requireActivity().finish()
        }
    }

    fun navigateToAboutScreen() {
        findNavController().navigate(R.id.action_profileFragment_to_aboutFragment)
    }

    fun navigateToLeaderboardScreen() {
        findNavController().navigate(R.id.action_profileFragment_to_leaderboardFragment)
    }
}
