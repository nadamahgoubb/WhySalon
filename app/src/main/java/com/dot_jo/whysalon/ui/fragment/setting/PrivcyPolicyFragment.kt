package com.dot_jo.whysalon.ui.fragment.setting

import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseFragment
import com.dot_jo.whysalon.data.response.Client
import com.dot_jo.whysalon.databinding.FragmentPrivcyPolicyBinding
import com.dot_jo.whysalon.ui.activity.MainActivity
import com.dot_jo.whysalon.ui.fragment.profile.ProfileAction
import com.dot_jo.whysalon.ui.fragment.profile.ProfileViewModel
import com.dot_jo.whysalon.util.FileManager
import com.dot_jo.whysalon.util.PermissionManager
import com.dot_jo.whysalon.util.ext.hideKeyboard
import com.dot_jo.whysalon.util.ext.loadImage
import com.dot_jo.whysalon.util.observe
import com.dot_jo.whysalon.util.requestAppPermissions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.cardback
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class PrivcyPolicyFragment : BaseFragment<FragmentPrivcyPolicyBinding>() {
    private lateinit var parent: MainActivity
    private val mViewModel: ProfileViewModel by viewModels()
    override fun onFragmentReady() {
        setupUi()
         mViewModel.apply {
            mViewModel.getPrivacyPolicy()
            observe(viewState) {
                handleViewState(it)
            }
        }
        binding.swiperefreshHome.setOnRefreshListener {
            mViewModel.getPrivacyPolicy()
            binding.swiperefreshHome.isRefreshing = false
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

            is ProfileAction.ShowPrivacy -> {
binding.tvPrivacy.setText(action.data)            }

            else -> {

            }
        }
    }

    private fun setupUi() {
        parent = requireActivity() as MainActivity
        parent.showBottomBar(false)
        parent.showToolbar(true)
        parent.setTitle(resources.getString(R.string.privacy_and_usage_policy))
        parent.showNotifactionFragment(false)
        //      parent.binding.ivIconNotifaction.isVisible= false
        parent.showback(true)

        parent.cardback.setOnClickListener {
            activity?.onBackPressed()
        }
        }



}