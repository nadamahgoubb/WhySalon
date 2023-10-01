package com.dot_jo.whysalon.ui.activity

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseActivity
import com.dot_jo.whysalon.databinding.ActivityAuthBinding
import com.dot_jo.whysalon.util.Constants
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : BaseActivity<ActivityAuthBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavController()
        binding.progress = baseShowProgress
    }
    private fun setupNavController() {
        binding.progress =  baseShowProgress

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        try {
            val inflater = navController.navInflater
            val graph = inflater.inflate(R.navigation.auth_navigation)
            val extras = intent.extras
            if (extras != null) {
                val value = extras.getInt(Constants.Start)

                if (value == Constants.login) {
                    graph.setStartDestination(R.id.loginFragment)

                } else  if (value == Constants.walk_throught) {
                    graph.setStartDestination(R.id.walkThroughtFragment)
                }
                else {
                    graph.setStartDestination(R.id.splashFragment)
                }

                val navController = navHostFragment.navController
                navController.setGraph(graph, intent.extras)
            }
        }
        catch (e:Exception){

        }



    }


}