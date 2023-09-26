package com.dot_jo.whysalon.ui.fragment.setting


import android.content.Intent
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseFragment
import com.dot_jo.whysalon.data.PrefsHelper
import com.dot_jo.whysalon.databinding.FragmentChangePasswordBinding
import com.dot_jo.whysalon.ui.activity.AuthActivity
import com.dot_jo.whysalon.ui.activity.MainActivity
import com.dot_jo.whysalon.ui.fragment.profile.ProfileAction
import com.dot_jo.whysalon.ui.fragment.profile.ProfileViewModel
import com.dot_jo.whysalon.util.Constants
import com.dot_jo.whysalon.util.ext.hideKeyboard
import com.dot_jo.whysalon.util.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.cardback

@AndroidEntryPoint
class ChangePasswordFragment : BaseFragment<FragmentChangePasswordBinding>() {
    private lateinit var parent: MainActivity

    val mViewModel :ProfileViewModel by viewModels()
    override fun onFragmentReady() {
  onClick()
        setupUi()
        mViewModel.apply {

            observe(viewState) {
           handleViewState(it) }
        }

    }

    private fun onClick() {
        binding.btnSubmit.setOnClickListener {

mViewModel.isValidParamsChangePass(binding.etCurrentPass.text.toString(),
    binding.etNewPass.text.toString(),binding.etConfirmPass.text.toString(),
    )
        }
    }
    private fun setupUi() {
        parent = requireActivity() as MainActivity
        parent.showBottomBar(false)
        parent.showToolbar(true)
        parent.setToolbarTitle(resources.getString(R.string.change_password))

        parent.showNotifactionFragment(false)
        parent.showback(true)
        parent.binding.card.isVisible= false

        parent.cardback.setOnClickListener {
           findNavController().navigateUp()
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

            is ProfileAction.ChangePassword -> {
showToast(action.data.scalar)
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

}