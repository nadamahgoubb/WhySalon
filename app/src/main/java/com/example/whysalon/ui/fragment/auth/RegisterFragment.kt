package com.example.whysalon.ui.fragment.auth

import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.whysalon.R
import com.example.whysalon.base.BaseFragment
import com.example.whysalon.data.PrefsHelper
import com.example.whysalon.databinding.FragmentLoginBinding
import com.example.whysalon.databinding.FragmentRegisterBinding
import com.example.whysalon.ui.activity.MainActivity
import com.example.whysalon.util.ext.showActivity


class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    override fun onFragmentReady() {
        binding.tvSignin.setPaintFlags(binding.tvSignin.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

        binding.tvSignin.setOnClickListener {
          activity?.onBackPressed()
        }
        binding.btnContineAsGuest.setOnClickListener {
            PrefsHelper.saveToken(null)
            showActivity(MainActivity::class.java, clearAllStack = true)        }

    }
}