package com.dot_jo.whysalon.ui.fragment.auth

import android.graphics.Paint
import androidx.fragment.app.viewModels
import com.dot_jo.whysalon.base.BaseFragment
import com.dot_jo.whysalon.data.PrefsHelper
import com.dot_jo.whysalon.databinding.FragmentRegisterBinding
import com.dot_jo.whysalon.ui.activity.MainActivity
import com.dot_jo.whysalon.util.ext.hideKeyboard
import com.dot_jo.whysalon.util.ext.showActivity
import com.dot_jo.whysalon.util.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {


    private val mViewModel: AuthViewModel by viewModels()

    private fun onclick() {
        binding.tvSignin.setPaintFlags(binding.tvSignin.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

        binding.tvSignin.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.btnContineAsGuest.setOnClickListener {
mViewModel.continueAsGyest()
         }
        binding.btnSignup.setOnClickListener {
            mViewModel.isVaildRegisteration(
             binding.etName.text.toString(), binding.etEmail.text.toString() ,  binding.etPassword.text.toString(), binding.etPasswordComfirm.text.toString() )
        }
    }

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

            is AuthAction.RegisterSucess -> {
                showProgress(false)
                PrefsHelper.saveUserData(action.data)

                PrefsHelper.saveToken(action.data.client?.token)
                gotoHome()

            }
            is AuthAction.ContinueAsGuest -> {
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

    private fun gotoHome() {
        showActivity(MainActivity::class.java, clearAllStack = true)

    }
}