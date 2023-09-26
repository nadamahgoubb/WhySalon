package com.dot_jo.whysalon.ui.fragment.setting


import android.content.Intent
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseFragment
import com.dot_jo.whysalon.data.PrefsHelper
import com.dot_jo.whysalon.databinding.FragmentSettingBinding
import com.dot_jo.whysalon.ui.activity.AuthActivity
import com.dot_jo.whysalon.ui.activity.MainActivity
import com.dot_jo.whysalon.ui.fragment.profile.ProfileAction
import com.dot_jo.whysalon.ui.fragment.profile.ProfileViewModel
import com.dot_jo.whysalon.util.Constants
import com.dot_jo.whysalon.util.ext.hideKeyboard
import com.dot_jo.whysalon.util.ext.loadImage
import com.dot_jo.whysalon.util.ext.showActivity
import com.dot_jo.whysalon.util.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_setting.checkbox

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>() {
    private lateinit var parent: MainActivity
    private val mViewModel: ProfileViewModel by viewModels()

    override fun onFragmentReady() {
        setupUi()
        onClick()


        mViewModel.apply {
            if (PrefsHelper.getUserData() != null) {
                mViewModel.showProfile()
            }
            observe(viewState) {
                handleViewState(it)
            }
        }

        if (PrefsHelper.getUserData() == null) {
            binding.headerEmptyState.isVisible = true
            binding.headerData.isVisible = false
            binding.btnLogin.isVisible = true
            binding.btnLogout.isVisible = false
            binding.lytNotifaction.isVisible = false
        } else {
            binding.headerEmptyState.isVisible = false
            binding.btnLogin.isVisible = false
            binding.btnLogout.isVisible = true


        }
    }

    private fun onClick() {
        binding.headerData.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }
        binding.tvContactUs.setOnClickListener {
            findNavController().navigate(R.id.contactUsFragment)
        }
        binding.btnLogout.setOnClickListener {
            findNavController().navigate(R.id.logoutDialog)
        }
        binding.tvPrivacy.setOnClickListener {
            findNavController().navigate(R.id.privcyPolicyFragment)
        }
        binding.tvTerms.setOnClickListener {
            findNavController().navigate(R.id.termsAndConitionFragment)
        }
        binding.tvAboutus.setOnClickListener {
            findNavController().navigate(R.id.aboutusFragment)
        }
        binding.btnLogin.setOnClickListener {
            PrefsHelper.clear()
            var intent = Intent(activity, AuthActivity::class.java)
            intent.putExtra(Constants.Start, Constants.login)
            startActivity(intent)
            activity?.finish()
        }
        binding.lytNotifaction.setOnClickListener {
            findNavController().navigate(R.id.notifactionFragment)
        }

        binding.checkboxNotifaction.setOnClickListener {


            mViewModel.changeNotifactionStatus()
        }
        binding.checkbox.setOnClickListener {
            if (checkbox.isChecked) {
                PrefsHelper.setLanguage(Constants.AR)
                showActivity(MainActivity::class.java, clearAllStack = true)
            } else {
                PrefsHelper.setLanguage(Constants.EN)
                showActivity(MainActivity::class.java, clearAllStack = true)
            }
        }
    }

    private fun loadData() {
        binding.ivProfile.loadImage(
            PrefsHelper.getUserData()?.client?.image,
            isCircular = true,
            placeHolderOnFsImage = R.drawable.empty_user_
        )
        binding.tvName.setText(PrefsHelper.getUserData()?.client?.name)
        binding.checkboxNotifaction.isChecked = PrefsHelper.getUserData()?.client?.notify == "1"
        binding.headerData.isVisible = true
        binding.lytNotifaction.isVisible = true
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

            is ProfileAction.ShowProfile -> {

                PrefsHelper.saveUserData(action.data)
                loadData()
            }

            is ProfileAction.NotifactionChanged -> {
                action.data.notify?.let {
                    binding.checkboxNotifaction.isChecked = it
                }
                mViewModel.showProfile()
            }

            else -> {

            }
        }
    }

    private fun setupUi() {
        parent = requireActivity() as MainActivity
        parent.showBottomBar(true)
        parent.showToolbar(true)
        parent.showNotifactionFragment(false)
        parent.setToolbarTitle(resources.getString(R.string.setting))
        parent.showback(false)

binding.checkbox.isChecked = PrefsHelper.getLanguage()==Constants.AR
    }
}