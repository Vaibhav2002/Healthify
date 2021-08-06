package com.vaibhav.healthify.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.vaibhav.chatofy.util.makeStatusBarTransparent
import com.vaibhav.healthify.R
import com.vaibhav.healthify.databinding.ActivityAuthBinding
import com.vaibhav.healthify.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityAuthBinding::inflate)
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        navController = findNavController(R.id.fragmentContainerView)
        makeStatusBarTransparent()
    }

    private fun loginUser() {
        val auth0 = Auth0(getString(R.string.client), getString(R.string.domain))
        WebAuthProvider
            .login(auth0)
            .withScheme(getString(R.string.scheme))
            .withScope("openid profile email")
            .start(
                this,
                object : Callback<Credentials, AuthenticationException> {
                    override fun onFailure(error: AuthenticationException) {
                        Timber.d("Failure ${error.message}")
                    }

                    override fun onSuccess(result: Credentials) {
                        Timber.d("Success ${result.idToken}")
                    }
                }
            )
    }
}
