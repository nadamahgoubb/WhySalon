package com.dot_jo.whysalon.ui.activity

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseActivity
import com.dot_jo.whysalon.data.PrefsHelper
import com.dot_jo.whysalon.databinding.ActivityMainBinding
import com.dot_jo.whysalon.util.ext.isNull
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {


    lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavController()
        binding.progress = baseShowProgress
        binding.ivIconNotifaction.setOnClickListener {
            navController.navigate(R.id.notifactionFragment)
        }
        binding.ivCancel.setOnClickListener {
            onBackPressed()
        }
        if (PrefsHelper.getUserData().isNull()){
            binding.ivIconNotifaction.isVisible= false

        }else{
             binding.ivIconNotifaction.isVisible= true
        }
    }

    private fun setupNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController
        binding.navView.setupWithNavController(navController)

        binding.navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.bookingFragment -> {
                    if (PrefsHelper.getUserData().isNull()) {

                        navController.navigate(R.id.loginFirstDialog)
                    } else {
                        navController.navigate(R.id.bookingFragment)
                    }
                    true
                }
                R.id.basketFragment -> {


                     if (PrefsHelper.getUserData().isNull()) {

                        navController.navigate(R.id.loginFirstDialog)
                    } else {
                          navController.navigate(
                             R.id.basketFragment,
                             null,
                             NavOptions.Builder().setPopUpTo(R.id.homeFragment, false).build()
                         )    }
                    true
                }
                R.id.servicesFragment -> {


                    navController.navigate(R.id.servicesFragment)

                    true
                }
                R.id.settingFragment -> {

                    navController.navigate(R.id.settingFragment)

                    true
                }

                else -> {
                    true
                }
            }
        }
    }

        fun showToolbar(show: Boolean) {

            binding.header.isVisible = show
        }

        fun showBottomBar(show: Boolean) {

            binding.navView.isVisible = show
        }

        fun setTitle(title: String) {

            binding.tvTitle.setText(title)
        }

        fun showback(show: Boolean) {

            binding.cardback.isVisible = show
        }

        fun showNotifactionFragment(show: Boolean) {
            if (show) {
                binding.tvTitle.setText(resources.getString(R.string.notification))
                binding.ivIconNotifaction.isVisible = false
                binding.ivCancel.isVisible = true

            } else {
                binding.ivIconNotifaction.isVisible = true
                binding.ivCancel.isVisible = false
            }
        }
    }