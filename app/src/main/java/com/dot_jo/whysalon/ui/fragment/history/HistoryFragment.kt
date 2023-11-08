package com.dot_jo.whysalon.ui.fragment.history

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseFragment
import com.dot_jo.whysalon.data.response.BookingsItem
import com.dot_jo.whysalon.databinding.FragmentHistoryBinding
import com.dot_jo.whysalon.ui.activity.MainActivity
import com.dot_jo.whysalon.ui.adapter.booking.HistoryItem
import com.dot_jo.whysalon.ui.adapter.booking.HistoryWithYearAdapter
import com.dot_jo.whysalon.ui.fragment.booking.BookingAction
import com.dot_jo.whysalon.ui.fragment.booking.BookingViewModel
import com.dot_jo.whysalon.ui.interfaces.HistoryClickListener
import com.dot_jo.whysalon.util.Constants
import com.dot_jo.whysalon.util.ext.hideKeyboard
import com.dot_jo.whysalon.util.ext.init
import com.dot_jo.whysalon.util.ext.showActivity
import com.dot_jo.whysalon.util.getYearFromDate
import com.dot_jo.whysalon.util.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.roundToInt

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding>(), HistoryClickListener {
    val mViewModel: BookingViewModel by viewModels()


    lateinit var adapter: HistoryWithYearAdapter
    private lateinit var parent: MainActivity
    private var orderId: String = ""
    var item: BookingsItem? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onFragmentReady() {
        setupUi()
        initAdapter()

        mViewModel.apply {
            getHistoryList()
            observe(viewState) {
                handleViewState(it)
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleViewState(action: BookingAction) {
        when (action) {
            is BookingAction.ShowLoading -> {
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }

            is BookingAction.ShowHistoryList -> {
                showProgress(false)
                if (action.data.bookings.isNullOrEmpty()) {
                    binding.rvHistory.isVisible = false
                    binding.lytEmptyState.isVisible = true
                } else {
                    binding.lytEmptyState.isVisible = false
                    binding.rvHistory.isVisible = true

                    loadData(action.data.bookings)
                }
            }

            is BookingAction.ShowReBooking -> {
                findNavController().navigate(
                    R.id.chooseBarberFragment, bundleOf(
                        Constants.TOTAL to action.data.finalTotal?.toDoubleOrNull()
                            ?.roundToInt().toString(),
                        Constants.ORDER_ID to orderId
                    )
                )

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
        parent.showBottomBar(false)
        parent.showToolbar(true)
        parent.setToolbarTitle(resources.getString(R.string.my_history))
        parent.showback(true)
        parent.showNotifactionFragment(false)

        parent.cardback.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.btnMakeappon.setOnClickListener {

            showActivity(MainActivity::class.java , clearAllStack = true)           }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadData(bookings: ArrayList<BookingsItem>) {

        var map = bookings.groupBy {
            it.date?.let { it1 -> getYearFromDate(it1) }
        } as Map<Int, ArrayList<BookingsItem>>

        val myItems: ArrayList<HistoryItem> = arrayListOf()
        map.forEach {
            myItems.add(HistoryItem.HeaderItem(it.key))
            myItems.add(HistoryItem.historyItem(map.getValue(it.key).get(0)))
        }

        adapter.list = myItems as MutableList<HistoryItem>
        adapter.mapp = map as MutableMap<Int, ArrayList<BookingsItem>>
        adapter.notifyDataSetChanged()

    }

    private fun initAdapter() {
        adapter = HistoryWithYearAdapter(this)
        binding.rvHistory.init(requireContext(), adapter, 2)

    }


    override fun onHistoryClickLisenter(id: BookingsItem) {
        orderId = id.id!!
        mViewModel.reBookingItem(id.id!!)
    }

}