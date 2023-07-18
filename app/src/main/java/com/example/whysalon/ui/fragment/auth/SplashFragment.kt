package com.example.whysalon.ui.fragment.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.whysalon.R
import com.example.whysalon.base.BaseFragment
import com.example.whysalon.data.PrefsHelper
import com.example.whysalon.databinding.FragmentSplashBinding
import com.example.whysalon.ui.activity.MainActivity
import com.example.whysalon.util.Constants
import com.example.whysalon.util.ext.showActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>() {


    override fun onFragmentReady() {

        binding.btnAr.setOnClickListener {
            binding.btnAr.setBackgroundResource(R.drawable.bg_btn_white)
            binding.btnAr.setTextColor(resources.getColor(R.color.black))
            binding.btnEn.setBackgroundResource(R.drawable.bg_btn_black_white_border)
            binding.btnEn.setTextColor(resources.getColor(R.color.white))

        PrefsHelper.setLanguage(Constants.EN)
        nextScreen()
        }
        binding.btnEn.setOnClickListener {

            PrefsHelper.setLanguage(Constants.EN)
            nextScreen()

        }

    }

    private fun nextScreen() {
        if (PrefsHelper.getToken().isNullOrEmpty()  ) {
            findNavController().navigate(
                R.id.walkThroughtFragment,
                null,
                NavOptions.Builder().setPopUpTo(R.id.splashFragment, true).build()
            )
        } else {
            showActivity(MainActivity::class.java, clearAllStack = true)
        }    }



}