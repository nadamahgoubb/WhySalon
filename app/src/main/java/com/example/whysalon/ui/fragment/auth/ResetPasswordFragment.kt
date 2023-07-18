package com.example.whysalon.ui.fragment.auth


import android.graphics.Paint
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.whysalon.R
import com.example.whysalon.base.BaseFragment
import com.example.whysalon.databinding.FragmentResetPasswordBinding

class ResetPasswordFragment : BaseFragment<FragmentResetPasswordBinding>() {

    var state = 1

    override fun onFragmentReady() {

        state1()
        onClick()
        onBack()
    }

    private fun onClick() {
        binding.tvResend.setPaintFlags(binding.tvResend.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        binding.btnSend.setOnClickListener {
            state2()
        }
        binding.btnDone.setOnClickListener {
            state3()
        }
        binding.btnReset.setOnClickListener {
            findNavController().navigate(
                R.id.loginFragment,null,
                NavOptions.Builder().setPopUpTo(R.id.loginFragment, true).build())        }

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
                                    requireActivity().onBackPressed()
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