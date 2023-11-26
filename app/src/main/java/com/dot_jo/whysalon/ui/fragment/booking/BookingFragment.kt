package com.dot_jo.whysalon.ui.fragment.booking

import android.annotation.SuppressLint
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseFragment
import com.dot_jo.whysalon.data.response.BookingsItem
import com.dot_jo.whysalon.databinding.FragmentBookingBinding
import com.dot_jo.whysalon.ui.activity.MainActivity
import com.dot_jo.whysalon.ui.adapter.booking.BookingAdapter
import com.dot_jo.whysalon.ui.dialog.CancelBookingDialog
import com.dot_jo.whysalon.ui.interfaces.CancelBookingListener
import com.dot_jo.whysalon.ui.interfaces.ConfirmCancelBookingListener
import com.dot_jo.whysalon.util.ext.hideKeyboard
import com.dot_jo.whysalon.util.ext.init
import com.dot_jo.whysalon.util.ext.showActivity
import com.dot_jo.whysalon.util.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookingFragment : BaseFragment<FragmentBookingBinding>(), CancelBookingListener {
    private lateinit var parent: MainActivity
    val mViewModel: BookingViewModel by viewModels()
    lateinit var adapter: BookingAdapter

    var item: BookingsItem? = null
    override fun onFragmentReady() {
        setupUi()
        initAdapter()
        mViewModel.apply {
            getBookingList()
            observe(viewState) {
                handleViewState(it)
            }
        }
        /*
        binding.swiperefreshHome.setOnRefreshListener {
            mViewModel.getBookingList()

            binding.swiperefreshHome.isRefreshing = false
        }*/
    }

    private fun initAdapter() {
        adapter = BookingAdapter(this)
        binding.rvBookings.init(requireContext(), adapter, 1)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleViewState(action: BookingAction) {
        when (action) {
            is BookingAction.ShowLoading -> {
                binding.shimmer.startShimmerAnimation()
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }

            is BookingAction.ShowBookingDeleted -> {
                item?.let { adapter.removeItem(it) }
                adapter.notifyDataSetChanged()
                showProgress(false)
            }

            is BookingAction.ShowBookingList -> {
                binding.shimmer.isVisible = false
                binding.shimmer.stopShimmerAnimation()

                if (action.data.bookings.isNullOrEmpty()) {
                    binding.lytData.isVisible = false
                    binding.lytEmptyState.isVisible = true
                } else {
                    adapter.list = action.data.bookings

                    adapter.notifyDataSetChanged()
                }
            }

            is BookingAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401") == true) {
                    findNavController().navigate(R.id.loginFirstDialog)
                } else {
                    showToast(action.message)
                }
                showProgress(false)

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
        parent.showback(false)
        parent.setToolbarTitle(resources.getString(R.string.yourbooking))
        binding.btnHistory.setOnClickListener {
            findNavController().navigate(R.id.historyFragment)
        }
        binding.btnGoHistory.setOnClickListener {
            findNavController().navigate(R.id.historyFragment)
        }
        binding.btnMakeappon.setOnClickListener {   findNavController().navigate(
            R.id.homeFragment,
            null,
            NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build()
        )     }

    }

    override fun onCancelBooking(item: BookingsItem?, position: Int) {
        this.item = item
        if (position == -1) {
            binding.lytEmptyState.isVisible = true

            binding.lytData.isVisible = false
        } else {
            CancelBookingDialog.newInstance(object : ConfirmCancelBookingListener {


                override fun onConfirmBooking() {
                    item?.id?.let { mViewModel.deleteBookingItem(it) }
                }
            })            .show(childFragmentManager, CancelBookingDialog::class.java.canonicalName)

        }
    }
}