package com.dot_jo.whysalon.ui.fragment.setting

import android.content.Intent
import android.net.Uri
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseFragment
import com.dot_jo.whysalon.data.response.ContactUsResponse
import com.dot_jo.whysalon.databinding.FragmentContactUsBinding
import com.dot_jo.whysalon.ui.activity.MainActivity
import com.dot_jo.whysalon.ui.fragment.profile.ProfileAction
import com.dot_jo.whysalon.ui.fragment.profile.ProfileViewModel
import com.dot_jo.whysalon.util.ext.hideKeyboard
import com.dot_jo.whysalon.util.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class ContactUsFragment : BaseFragment<FragmentContactUsBinding>() {
    private lateinit var parent: MainActivity
    private val mViewModel: ProfileViewModel by viewModels()
    var facebookUrl = ""
    var snapchatUrl = ""
    var instagramUrl = ""

    override fun onFragmentReady() {
        setupUi()
        onClick()
        mViewModel.apply {
            mViewModel.getContactusData()
            observe(viewState) {
                handleViewState(it)
            }
        }
        binding.swiperefreshHome.setOnRefreshListener {
            mViewModel.getContactusData()
            binding.swiperefreshHome.isRefreshing = false
        }
    }

    private fun onClick() {
        binding.ivFacbook.setOnClickListener {
            openUrl(facebookUrl)
        }
        binding.ivInstagram.setOnClickListener {
            openUrl(instagramUrl)
        }
        binding.ivTwiter.setOnClickListener {
            openUrl(snapchatUrl)
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

            is ProfileAction.ShowContactUsResponse -> {
                loadData(action.data)
            }

            else -> {

            }
        }
    }

    private fun loadData(data: ContactUsResponse) {
        var days = ""
        var numbers = ""
        data?.let {

            binding.tvAddress.setText(it.address)

            data.contactUsPhones.forEach {
                numbers = numbers + it.phone + "\n"
            }
            data.contactUsDay.forEach {
                if (it.toDay == null) {
                    days =
                        days + it.fromDay?.name + " " + "(" + it.fromTime + " - " + it.toTime + ")" + "\n"
                } else {
                    days =
                        days + it.fromDay?.name + " - " + it.toDay?.name + " " + "(" + it.fromTime + " - " + it.toTime + ")" + "\n"

                }
            }


            binding.tvPhone.setText(numbers)
            binding.tvWorkingHours.setText(days)

            binding.lytData.isVisible= true
 it.facebookUrl?.let {facebookUrl = it  }
 it.instagramUrl?.let {instagramUrl = it  }
 it.snapchat?.let {snapchatUrl = it  }


        }
    }

    private fun openUrl(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    private fun setupUi() {
        parent = requireActivity() as MainActivity
        parent.showBottomBar(false)
        parent.showToolbar(true)
        parent.showNotifactionFragment(false)
        parent.setTitle(resources.getString(R.string.contact_us))
        parent.showback(true)

        parent.cardback.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}