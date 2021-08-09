package com.vaibhav.healthify.ui.auth.gettingStarted

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import com.vaibhav.healthify.R
import com.vaibhav.healthify.databinding.FragmentGettingStartedBinding
import com.vaibhav.healthify.ui.homeScreen.MainActivity
import com.vaibhav.healthify.ui.userDetailsInput.UserDetailsActivity
import com.vaibhav.healthify.util.showToast
import com.vaibhav.healthify.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class GettingStartedFragment : Fragment(R.layout.fragment_getting_started) {

    private val binding by viewBinding(FragmentGettingStartedBinding::bind)
    private val viewModel by viewModels<GettingStartedViewModel>()

    @Inject
    lateinit var auth0: Auth0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            loginUser()
        }
        collectUiState()
        collectUiEvents()
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                binding.btnLogin.isEnabled = it.isButtonEnabled
                binding.loadingLayout.loadingLayout.isVisible = it.isLoading
            }
        }
    }

    private fun collectUiEvents() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.events.collect {
                when (it) {
                    is GettingStartedScreenEvents.ShowToast -> requireContext().showToast(it.message)
                    GettingStartedScreenEvents.Logout -> logoutUser()
                    GettingStartedScreenEvents.NavigateToUserDetailsScreen -> navigateToUserDetailsScreen()
                    GettingStartedScreenEvents.NavigateToHomeScreen -> navigateToHomeScreen()
                }
            }
        }
    }

    private fun loginUser() {
        viewModel.startLoading()
        WebAuthProvider
            .login(auth0)
            .withScheme(getString(R.string.scheme))
            .withScope("openid profile email read:current_user update:current_user_metadata")
            .start(
                requireContext(),
                object : Callback<Credentials, AuthenticationException> {
                    override fun onFailure(error: AuthenticationException) {
                        viewModel.sendError(error.message.toString())
                    }

                    override fun onSuccess(result: Credentials) {
                        getUserProfile(result.accessToken)
                    }
                }
            )
    }

    private fun getUserProfile(accessToken: String) {
        val client = AuthenticationAPIClient(auth0)
        client.userInfo(accessToken)
            .start(object : Callback<UserProfile, AuthenticationException> {
                override fun onFailure(error: AuthenticationException) {
                    viewModel.sendError(error.message.toString())
                }

                override fun onSuccess(result: UserProfile) {
                    viewModel.saveUser(result)
                    viewModel.loginComplete()
                }
            })
    }

    private fun logoutUser() {
        WebAuthProvider
            .logout(auth0)
            .withScheme(getString(R.string.scheme))
            .start(
                requireContext(),
                object : Callback<Void?, AuthenticationException> {
                    override fun onFailure(error: AuthenticationException) {
                        viewModel.logoutFailed()
                    }

                    override fun onSuccess(result: Void?) {
                        viewModel.logoutComplete()
                    }
                }
            )
    }

    private fun navigateToHomeScreen() {
        Intent(requireContext(), MainActivity::class.java).also { intent ->
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun navigateToUserDetailsScreen() {
        Intent(requireContext(), UserDetailsActivity::class.java).also { intent ->
            startActivity(intent)
            requireActivity().finish()
        }
    }
}
