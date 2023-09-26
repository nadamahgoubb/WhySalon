package com.dot_jo.whysalon.ui.fragment.setting

import android.content.Intent
import android.graphics.Paint
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseFragment
import com.dot_jo.whysalon.data.PrefsHelper
import com.dot_jo.whysalon.databinding.FragmentDeleteBinding
import com.dot_jo.whysalon.ui.activity.AuthActivity
import com.dot_jo.whysalon.ui.activity.MainActivity
import com.dot_jo.whysalon.ui.fragment.profile.ProfileAction
import com.dot_jo.whysalon.ui.fragment.profile.ProfileViewModel
import com.dot_jo.whysalon.util.Constants
import com.dot_jo.whysalon.util.ext.hideKeyboard
import com.dot_jo.whysalon.util.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class DeleteFragment : BaseFragment<FragmentDeleteBinding>() {
    private val mViewModel: ProfileViewModel by viewModels()
    private lateinit var parent: MainActivity
    var agreed = false
    override fun onFragmentReady() {
        setupUi()
        onClick()
        mViewModel.apply {
            mViewModel.showProfile()
            observe(viewState) {
                handleViewState(it)
            }
        }
    }

    private fun onClick() {
        parent.cardback.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.checkbox.setOnClickListener {
            if (binding.checkbox.isChecked == true) {
                binding.btnDelete.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.red_dark)
                agreed = true
                //backgroundTintMode= resources.getColor(R.color.red_dark)
            } else {
                binding.btnDelete.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.red_100)
                agreed = false

            }
        }
        binding.btnDelete.setOnClickListener {
            if (agreed) {
                mViewModel.deleteAccount()

            } else {
                showToast(getString(R.string.confirm_delete))
            }
        }
        binding.btnKeepAccount.setOnClickListener {
            findNavController()?.navigateUp()
        }
    }

    private fun handleViewState(action: ProfileAction) {
        when (action) {
            is ProfileAction.ShowLoading -> {
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }

            is ProfileAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401") == true) {
                    findNavController().navigate(R.id.loginFirstDialog)
                } else {
                    showToast(action.message)
                }
                showProgress(false)

            }


            is ProfileAction.DeleteProfile -> {
                PrefsHelper.clear()
                var intent = Intent(activity, AuthActivity::class.java)
                intent.putExtra(Constants.Start, Constants.login)
                startActivity(intent)
                activity?.finish()

            }

            else -> {

            }
        }
    }

    private fun setupUi() {
        binding.btnKeepAccount.setPaintFlags(binding.btnKeepAccount.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        parent = requireActivity() as MainActivity
        parent.showBottomBar(false)
        parent.showToolbar(true)
        parent.showNotifactionFragment(false)
        parent.setToolbarTitle(resources.getString(R.string.delete_account))
        parent.showback(true)


    }
}