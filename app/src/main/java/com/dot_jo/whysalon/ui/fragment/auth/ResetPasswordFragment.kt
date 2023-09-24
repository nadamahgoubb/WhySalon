package com.dot_jo.whysalon.ui.fragment.auth


import android.graphics.Paint
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseFragment
import com.dot_jo.whysalon.databinding.FragmentResetPasswordBinding
import com.dot_jo.whysalon.util.ext.hideKeyboard
import com.dot_jo.whysalon.util.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordFragment : BaseFragment<FragmentResetPasswordBinding>() {
    private val mViewModel: AuthViewModel by viewModels()

    var state = 1

    override fun onFragmentReady() {

        state1()
        onClick()
        onBack()
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

            is AuthAction.EmailChecked -> {
                showProgress(false)
if(action.data.exists==true)  state2()
                else showToast(resources.getString(R.string.email_not_exist))
            }
            is AuthAction.OtpSuccess -> {
                showProgress(false)

                findNavController().navigate(
                    R.id.loginFragment,null,
                    NavOptions.Builder().setPopUpTo(R.id.loginFragment, true).build())
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

    private fun onClick() {
        binding.tvResend.setPaintFlags(binding.tvResend.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        binding.btnSend.setOnClickListener {
            if(binding.etEmail.text.isNullOrBlank() )  showToast(resources.getString(R.string.empty_msg_email))
            else {
                mViewModel.email = binding.etEmail.text.toString()
                mViewModel.checkEmail(mViewModel.email!!)
             }
        }
        binding.tvResend.setOnClickListener {

            mViewModel.email?.let { it1 -> mViewModel.checkEmail(it1) }

        }
        binding.btnDone.setOnClickListener {
            if(!binding.etOtp.otp.isNullOrBlank()){
                mViewModel.otp = binding.etOtp.otp
                state3()
            }
        }

     binding.btnReset.setOnClickListener {
         mViewModel.isValidParamsChangePass(
             binding.etPassword.text.toString(),
             binding.etPasswordComfirm.text.toString(),
         )
     }
        binding.cardback.setOnClickListener {
            when (state) {
                1 -> {
                    requireActivity().onBackPressed()
                }

                2 -> state1()
                3 -> state2()

            }
        }
    }


    private fun onBack() {
        activity?.let {
            requireActivity().onBackPressedDispatcher.addCallback(it,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        when (state) {
                            1 -> {

                                if (isEnabled) {
                                    isEnabled = false
                               findNavController().navigateUp()
                                }
                            }
                            2 -> state1()
                            3 -> state2()

                        }
                    }
                })


        }
    }

    private fun state2() {
      state=2
        binding.tvEmailSendTo.setText(""+getString(R.string.enter_the_email_we_send_to) + " "+ mViewModel.email)
        binding.lytState1.isVisible = false
        binding.lytState2.isVisible = true
        binding.lytState3.isVisible = false
    }

    private fun state3() {
        state=3
        binding.lytState1.isVisible = false
        binding.lytState2.isVisible = false
        binding.lytState3.isVisible = true
    }

    private fun state1() {
        state=1
        binding.lytState1.isVisible = true
        binding.lytState2.isVisible = false
        binding.lytState3.isVisible = false
    }
}