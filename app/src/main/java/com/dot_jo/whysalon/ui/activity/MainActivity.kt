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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
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
import com.dot_jo.whysalon.ui.fragment.home.HomeAction
import com.dot_jo.whysalon.ui.fragment.home.HomeViewModel
import com.dot_jo.whysalon.util.Constants
import com.dot_jo.whysalon.util.ext.getMyData
import com.dot_jo.whysalon.util.ext.isNull
import com.dot_jo.whysalon.util.observe
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
companion object
{
    const val MAIN_SCREEN_ACTION ="MAIN_SCREEN_ACTION"
}
    private val mViewModel: HomeViewModel by viewModels()

    lateinit var navController: NavController
    private var hasNotificationPermissionGranted = false
     private val fcmBroadcast by lazy { FcmBroadcast() }
    private val reciever = object : BroadcastReceiver() {
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

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavController()
        notificationPermission()
        binding.progress = baseShowProgress
         registerReceiver(reciever, IntentFilter(MAIN_SCREEN_ACTION))
        onClick()

         mViewModel.apply {
            getCart()

            observe(viewState) {
                handleViewState(it)
            }
        }

    }
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        var data: FcmResponse? = intent.getMyData<FcmResponse>(Constants.Notifaction)
        if (data == null) {
            navController.navigate(R.id.homeFragment)

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



        super.onNewIntent(intent)
    }


    private fun onClick() {
        binding.card.setOnClickListener {
             if(navController.currentDestination?.id?.equals(R.id.notifactionFragment) == true){
                navController.popBackStack()

            }else{
                navController.navigate(R.id.notifactionFragment)

            }
        }
        binding.ivIconNotifaction.isVisible = !PrefsHelper.getUserData().isNull()    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleViewState(action: HomeAction) {
        when (action) {
            is HomeAction.ShowLoading -> {


            }
  is HomeAction.ShowCartData -> {
      setBadge(action.data.carts.size)

            }




            else -> {

            }
        }
    }




    private fun setupNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController
        binding.navView.setupWithNavController(navController)
       // var badge = binding.navView.getOrCreateBadge(R.id.basketFragment);
      //  badge.isVisible = true;
// An icon only badge will be displayed unless a number is set:
     //   badge.number = 1;
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
                        navController.navigate(
                            R.id.bookingFragment,
                            null,
                            NavOptions.Builder().setPopUpTo(R.id.homeFragment, false).build()
                        )
                        //navController.navigate(R.id.bookingFragment)
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

                R.id.packagesFragment -> {

                    navController.navigate(
                        R.id.packagesFragment,
                        null,
                        NavOptions.Builder().setPopUpTo(R.id.homeFragment, false).build()
                    )
                   // navController.navigate(R.id.packagesFragment)

                    true
                }

                R.id.settingFragment -> {

                    navController.navigate(
                        R.id.settingFragment,
                        null,
                        NavOptions.Builder().setPopUpTo(R.id.homeFragment, false).build()
                    )
                    //navController.navigate(R.id.settingFragment)

                    true
                }

                else -> {
                    true
                }
            }
        }
    }

    fun setBadge(count: Int) {
        if (count == 0) {
            binding.navView.removeBadge(R.id.basketFragment)
        } else {
            val badge =   binding.navView.getOrCreateBadge(R.id.basketFragment) // previously showBadge
            badge.number = count
             badge.badgeGravity= BadgeDrawable.BOTTOM_END

             badge.backgroundColor = getColor(R.color.red_500)
            badge.badgeTextColor = getColor(R.color.white)
        }
    }
/*
    @SuppressLint("PrivateResource")
    private fun addBadge(position: Int) {
        // get badge container (parent)
        val bottomMenu = binding.navView.menu
        val v = bottomMenu?.findItem (R.id.basketFragment)

        // inflate badge from layout
     var   badge = LayoutInflater.from(this)
            .inflate(R.layout.badge_layout, binding.navView, false)

        // create badge layout parameter

        //    badge?.layoutParams?.let {
                val badgeLayout: FrameLayout.LayoutParams? = badge?.layoutParams?.let {
                    FrameLayout.LayoutParams(
                        it
                    )
                }
                    //.apply {
            //        gravity = Gravity.CENTER_HORIZONTAL
                 //   topMargin = resources.getDimension(com.intuit.sdp.R.dimen._4sdp).toInt()

                    // <dimen name="bagde_left_margin">8dp</dimen>
               //     leftMargin = resources.getDimension(com.intuit.sdp.R.dimen._4sdp).toInt()
          //      }
                v?.addView(badge, badgeLayout)
       //     }

        // add view to bottom bar with layout parameter
    }*/
    fun showToolbar(show: Boolean) {

        binding.header.isVisible = show
    }

    fun showBottomBar(show: Boolean) {

        binding.navView.isVisible = show
    }

    fun setToolbarTitle(title: String) {

        binding.tvTitle.setText(title)
    }

    fun showback(show: Boolean) {

        binding.cardback.isVisible = show
    }

    fun showNotifactionFragment(show: Boolean) {
   binding.card.isVisible= true

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