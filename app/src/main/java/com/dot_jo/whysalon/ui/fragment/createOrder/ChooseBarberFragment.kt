package com.dot_jo.whysalon.ui.fragment.createOrder

import android.annotation.SuppressLint
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseFragment
import com.dot_jo.whysalon.data.response.BarbarItem
import com.dot_jo.whysalon.databinding.FragmentChooseBarberBinding
import com.dot_jo.whysalon.ui.activity.MainActivity
import com.dot_jo.whysalon.ui.adapter.BarbarAdapter
import com.dot_jo.whysalon.ui.interfaces.BarbarClickListener
import com.dot_jo.whysalon.util.Constants
import com.dot_jo.whysalon.util.ext.hideKeyboard
import com.dot_jo.whysalon.util.ext.init
import com.dot_jo.whysalon.util.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.cardback

@AndroidEntryPoint
class ChooseBarberFragment : BaseFragment<FragmentChooseBarberBinding>(), BarbarClickListener {

    var state = 0
    private val mViewModel: CreateOrderViewModel by activityViewModels()
    lateinit var adapter: BarbarAdapter
    private lateinit var parent: MainActivity
    private lateinit var barbar: BarbarItem
    private var orderId: String = ""
    private fun initAdapter() {
        adapter = BarbarAdapter(this)
        binding.rvBarbars.init(requireContext(), adapter, 2)

    }

    override fun onFragmentReady() {
     state=0
        mViewModel.barbar = null
        setupUi()
        initAdapter()
        mViewModel.apply {
            getBarbars()

            observe(viewState) {
                handleViewState(it)
            }
        }
        binding.swiperefreshHome.setOnRefreshListener {
            mViewModel.getBarbars()

            binding.swiperefreshHome.isRefreshing = false
        }
    }

    private fun handleViewState(action: CreateOrderAction) {
        when (action) {
            is CreateOrderAction.ShowLoading -> {
                binding.shimmer.startShimmerAnimation()

                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }

            is CreateOrderAction.ShowBarbars -> {
                showProgress(false)
                binding.shimmer.isVisible = false
                binding.shimmer.stopShimmerAnimation()
                action.data.barbers?.let {
                    adapter.list = it
                    adapter.notifyDataSetChanged()
                }
            }


            is CreateOrderAction.ShowFailureMsg -> action.message?.let {
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


    @SuppressLint("UseCompatLoadingForDrawables")
    fun enablebtn(enable: Boolean) {
        if (enable) {
            binding.cardNext.background =
                resources.getDrawable(R.drawable.bg_btn_black_white_border)
            state = 1
        } else {
            binding.cardNext.background = resources.getDrawable(R.drawable.bg_btn_gray1)
            state = 0

        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupUi() {
        parent = requireActivity() as MainActivity
        parent.showBottomBar(false)
        parent.showToolbar(true)
        parent.showback(true)
        parent.showNotifactionFragment(false)

        parent.cardback.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.cardNext.setOnClickListener {
            if (state == 1) {

                findNavController().navigate(
                    R.id.calenderFragment,
                    bundleOf(
                        Constants.BARBER to barbar.name!!,
                        Constants.ORDER_ID to if (orderId == "") "" else orderId
                    )
                )

            }
        }

        parent.setTitle(resources.getString(R.string.choose_the_barber))
        mViewModel.total = arguments?.getString(Constants.TOTAL)
        this.orderId = if (arguments?.getString(Constants.ORDER_ID) != "") {
            arguments?.getString(Constants.ORDER_ID).toString()
        }else{
            ""
        }
        binding.tvTotal.text = mViewModel.total + resources.getString(R.string.sr)

    }

    override fun onBarbarClickListener(item: BarbarItem?) {
        mViewModel.barbar = item
        if (item != null) {
            barbar = item
        }
        if (item == null) {
            enablebtn(false)
        } else enablebtn(true)

    }
}