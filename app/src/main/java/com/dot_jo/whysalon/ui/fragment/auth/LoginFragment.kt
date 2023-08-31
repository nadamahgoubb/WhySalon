package com.dot_jo.whysalon.ui.fragment.auth

import android.graphics.Paint
 import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseFragment
import com.dot_jo.whysalon.data.PrefsHelper
import com.dot_jo.whysalon.databinding.FragmentLoginBinding
import com.dot_jo.whysalon.ui.activity.MainActivity
import com.dot_jo.whysalon.util.ext.hideKeyboard
import com.dot_jo.whysalon.util.ext.showActivity
import com.dot_jo.whysalon.util.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    private val mViewModel: AuthViewModel by viewModels()
    override fun onFragmentReady() {
        onclick()
        mViewModel.apply {
            observe(viewState) {
                handleViewState(it)
            }
        }
    }
    private fun handleViewState(action: AuthAction) {
        when (action) {
            is AuthAction.ShowLoading -> {
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }

            is AuthAction.LoginSuccess -> {
                showProgress(false)

                PrefsHelper.saveToken(action.data.client?.token)
                PrefsHelper.saveUserData(action.data)
                    gotoHome()

            }   is AuthAction.ContinueAsGuest -> {
                showProgress(false)
                PrefsHelper.saveToken(action.data.client?.token)
                PrefsHelper.saveUserData(null)
                    gotoHome()

            }

            is AuthAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401") == true) {
                    showToast(it.substring(3, it.length))
                } else {
                    showToast(action.message)
                }
                showProgress(false)

            }

            else -> {

            }
        }
    }

    private fun onclick() {
        binding.tvForgetPassword.setPaintFlags(binding.tvForgetPassword.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        binding.tvSignup.setPaintFlags(binding.tvSignup.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

        binding.tvSignup.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }
        binding.tvForgetPassword.setOnClickListener {
            findNavController().navigate(R.id.resetPasswordFragment)
        }
        binding.btnLogin.setOnClickListener {
            mViewModel.isVaildLogin(
                binding.etEmail.text.toString(), binding.etPassword.text.toString(), 0
            )
        }
        binding.btnGoogle.setOnClickListener {
            PrefsHelper.saveToken("nn")
            gotoHome()
//mViewModel.isVaildLogin(binding.etEmail.text.toString() , binding.etPassword.text.toString(),1)
        }
        binding.btnContineAsGuest.setOnClickListener {
         mViewModel.continueAsGyest()
        }
    }


    private fun gotoHome() {
        showActivity(MainActivity::class.java, clearAllStack = true)

    }
}