package com.example.whysalon.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.example.whysalon.R
import com.example.whysalon.base.BaseActivity
import com.example.whysalon.databinding.ActivityAuthBinding
import com.example.whysalon.util.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : BaseActivity<ActivityAuthBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavController()
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