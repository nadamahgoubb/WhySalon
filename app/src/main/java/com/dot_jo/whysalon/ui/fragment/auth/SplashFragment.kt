package com.dot_jo.whysalon.ui.fragment.auth

import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseFragment
import com.dot_jo.whysalon.data.PrefsHelper
import com.dot_jo.whysalon.databinding.FragmentSplashBinding
import com.dot_jo.whysalon.ui.activity.MainActivity
import com.dot_jo.whysalon.util.Constants
import com.dot_jo.whysalon.util.ext.showActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>() {


    override fun onPause() {
        lifecycleScope.cancel()
        super.onPause()
    }


    override fun onFragmentReady() {
        lifecycleScope.launch {
            delay(1500)
            nextScreen()

        }

    }

    private fun nextScreen() {
        val m = PrefsHelper.getToken()
        if (!PrefsHelper.getToken().isNullOrEmpty()) {
            showActivity(MainActivity::class.java, clearAllStack = true)
        } else {
            if (PrefsHelper.isLoggedBefore()) {

                findNavController().navigate(
                    R.id.loginFragment,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.splashFragment, true).build()
                )
            } else {
                PrefsHelper.setLoggedBefore(true)
                findNavController().navigate(
                    R.id.walkThroughtFragment,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.splashFragment, true).build()
                )

            }
        }
    }


}