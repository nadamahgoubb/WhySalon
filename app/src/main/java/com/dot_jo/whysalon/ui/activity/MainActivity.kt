package com.dot_jo.whysalon.ui.activity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseActivity
import com.dot_jo.whysalon.data.PrefsHelper
import com.dot_jo.whysalon.databinding.ActivityMainBinding
import com.dot_jo.whysalon.fcm.FcmBroadcast
import com.dot_jo.whysalon.fcm.FcmResponse
import com.dot_jo.whysalon.util.Constants
import com.dot_jo.whysalon.util.ext.getMyData
import com.dot_jo.whysalon.util.ext.isNull
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
companion object
{
    const val MAIN_SCREEN_ACTION ="MAIN_SCREEN_ACTION"
}

    lateinit var navController: NavController
    private var hasNotificationPermissionGranted = false
    private lateinit var reciever: Receiver
    private val fcmBroadcast by lazy { FcmBroadcast() }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavController()
        notificationPermission()
        binding.progress = baseShowProgress
        reciever = Receiver()
        registerReceiver(reciever, IntentFilter(MAIN_SCREEN_ACTION))
        binding.ivIconNotifaction.setOnClickListener {
            navController.navigate(R.id.notifactionFragment)
        }
        binding.ivCancel.setOnClickListener {
            navController.popBackStack()
        }
        binding.ivIconNotifaction.isVisible = !PrefsHelper.getUserData().isNull()
        handleNotificationIntent()
        registerReceiver(receiver, IntentFilter(MAIN_SCREEN_ACTION))
     }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            var data: FcmResponse? = intent.getMyData<FcmResponse>(Constants.Notifaction)
            if (data == null) {
                 /*     startActivity(
                         Intent(
                             this@MainActivity, MainActivity::class.java
                         )
                     )
     this@MainActivity.finish()*/
            } else {
                val barberId = data.BARBER_Id
                val orderId = data.ORDER_I
                val barber_image = data.BARBER
                navController.navigate(
                    R.id.rateBottomSheet, bundleOf(
                        Constants.BARBER_ID to barberId,
                        Constants.ORDER_ID to orderId,
                        Constants.BARBER to barber_image
                    )
                )
            }
        }
    }

    private fun handleNotificationIntent() {
        if (intent.action == Constants.OPEN_NOTIFICATION) {
            if (intent.hasExtra(Constants.BARBER_ID) && intent.hasExtra(Constants.ORDER_ID)) {
                val barberId = intent.getStringExtra(Constants.BARBER_ID)
                val orderId = intent.getStringExtra(Constants.ORDER_ID)
                val barber_image = intent.getStringExtra(Constants.BARBER)
                navController.navigate(
                    R.id.rateBottomSheet, bundleOf(
                        Constants.BARBER_ID to barberId,
                        Constants.ORDER_ID to orderId,
                        Constants.BARBER to barber_image
                    )
                )
            }
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
                        )
                    }
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

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(reciever)
    }

    private inner class Receiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {

            }
        }
    }

    private fun notificationPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        } else {
            hasNotificationPermissionGranted = true
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            hasNotificationPermissionGranted = isGranted
            if (!isGranted) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Build.VERSION.SDK_INT >= 33) {
                        if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                            showNotificationPermissionRationale()
                        } else {
                            showSettingDialog()
                        }
                    }
                }
            }
        }

    private fun showSettingDialog() {
        MaterialAlertDialogBuilder(
            this,
            com.google.android.material.R.style.MaterialAlertDialog_Material3
        )
            .setTitle("Notification Permission")
            .setMessage("Notification permission is required, Please allow notification permission from setting")
            .setPositiveButton("Ok") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showNotificationPermissionRationale() {

        MaterialAlertDialogBuilder(
            this,
            com.google.android.material.R.style.MaterialAlertDialog_Material3
        )
            .setTitle("Alert")
            .setMessage("Notification permission is required, to show notification")
            .setPositiveButton("Ok") { _, _ ->
                if (Build.VERSION.SDK_INT >= 33) {
                    notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}