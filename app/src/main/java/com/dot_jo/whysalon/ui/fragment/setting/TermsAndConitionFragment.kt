package com.dot_jo.whysalon.ui.fragment.setting

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseFragment
import com.dot_jo.whysalon.databinding.FragmentPrivcyPolicyBinding
import com.dot_jo.whysalon.ui.activity.MainActivity
import com.dot_jo.whysalon.ui.fragment.profile.ProfileAction
import com.dot_jo.whysalon.ui.fragment.profile.ProfileViewModel
import com.dot_jo.whysalon.util.ext.hideKeyboard
import com.dot_jo.whysalon.util.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.cardback

@AndroidEntryPoint
class TermsAndConitionFragment : BaseFragment<FragmentPrivcyPolicyBinding>() {
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

            is ProfileAction. ShowPrivacy -> {
binding.tvPrivacy.setText(action.data.terms_and_conditions)            }

            else -> {

            }
        }
    }

    private fun setupUi() {
        parent = requireActivity() as MainActivity
        parent.showBottomBar(false)
        parent.showToolbar(true)
        parent.setToolbarTitle(resources.getString(R.string.terms_condition))
        parent.showNotifactionFragment(false)
        //      parent.binding.ivIconNotifaction.isVisible= false
        parent.showback(true)

        parent.cardback.setOnClickListener {
            activity?.onBackPressed()
        }
        }



}