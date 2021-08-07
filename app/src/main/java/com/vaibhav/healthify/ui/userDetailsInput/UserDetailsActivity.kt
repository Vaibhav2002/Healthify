package com.vaibhav.healthify.ui.userDetailsInput

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.vaibhav.chatofy.util.makeStatusBarTransparent
import com.vaibhav.healthify.R
import com.vaibhav.healthify.databinding.ActivityUserDetailsBinding
import com.vaibhav.healthify.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailsActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityUserDetailsBinding::inflate)
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        makeStatusBarTransparent()
        navController = findNavController(R.id.fragmentContainerView2)
    }
}
