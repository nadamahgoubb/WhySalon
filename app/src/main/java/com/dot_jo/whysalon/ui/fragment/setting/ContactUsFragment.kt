package com.dot_jo.whysalon.ui.fragment.setting

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
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
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ContactUsFragment : BaseFragment<FragmentContactUsBinding>() {
    private lateinit var parent: MainActivity
    private val mViewModel: ProfileViewModel by viewModels()
    var facebookUrl = ""
    var tiktok_url = ""
    var instagramUrl = ""
    var lat :String? = null
    var lng  :String? = null

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
        binding.ivTictok.setOnClickListener {
            openUrl(tiktok_url)
        }
        binding.lytLocation.setOnClickListener {
            lat?.let { it1 -> lng?.let { it2 -> openMap(it1, it2) } }
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadData(data: ContactUsResponse) {
        var days = ""
        var numbers = ""
        data?.let {

            binding.tvAddress.setText(it.address)

            data.contactUsPhones.forEach {
                numbers = numbers +it.countryCode +" "+ it.phone + "\n"
            }
            data.contactUsDay.forEach {
                if (it.toDay == null) {
                    days =
                        days + it.fromDay?.name + " " + "(" + adjustTime(it.fromTime) + " - " +   adjustTime(it.toTime)+ ")" + "\n"
                } else {
                    days =
                        days + it.fromDay?.name + " - " + it.toDay?.name + " " + "(" +  adjustTime(it.fromTime )+ " - " +  adjustTime(it.toTime) + ")" + "\n"

                }
            }


            binding.tvPhone.setText(numbers)
            binding.tvWorkingHours.setText(days)

            binding.lytData.isVisible= true
 it.facebookUrl?.let {facebookUrl = it  }
 it.instagramUrl?.let {instagramUrl = it  }
 it.tiktok_url?.let {tiktok_url = it  }
 it.lat?.let {lat = it  }
 it.lng?.let {lng = it  }


        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun adjustTime(time:String?): String {
     return   LocalTime.parse(time!!).format(
            DateTimeFormatter.ofPattern("h:mma")) ?: ""
    }
    fun openMap(  latitude :String , longitude :String){
        val uri = "https://www.google.com.tw/maps/place/$latitude,$longitude"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        startActivity(intent)

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
        parent.setToolbarTitle(resources.getString(R.string.contact_us))
        parent.showback(true)

        parent.cardback.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}