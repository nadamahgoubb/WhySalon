package com.example.whysalon.ui.fragment.auth

import android.graphics.Paint
import androidx.navigation.fragment.findNavController
import com.example.whysalon.R
import com.example.whysalon.base.BaseFragment
import com.example.whysalon.data.PrefsHelper
import com.example.whysalon.databinding.FragmentLoginBinding
import com.example.whysalon.ui.activity.MainActivity
import com.example.whysalon.util.ext.showActivity


class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    override fun onFragmentReady() {
        binding.tvForgetPassword.setPaintFlags(binding.tvForgetPassword.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        binding.tvSignup.setPaintFlags(binding.tvSignup.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

        binding.tvSignup.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }
        binding.tvForgetPassword.setOnClickListener {
            findNavController().navigate(R.id.resetPasswordFragment)
        }
        binding.btnLogin.setOnClickListener {
            PrefsHelper.saveToken("f")
            showActivity(MainActivity::class.java, clearAllStack = true)
        }
        binding.btnContineAsGuest.setOnClickListener {
            PrefsHelper.saveToken(null)
            showActivity(MainActivity::class.java, clearAllStack = true)
        }

    }
}