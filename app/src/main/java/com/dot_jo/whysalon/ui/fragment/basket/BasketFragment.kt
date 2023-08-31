package com.dot_jo.whysalon.ui.fragment.basket

import android.annotation.SuppressLint
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseFragment
import com.dot_jo.whysalon.data.response.CartResponse
import com.dot_jo.whysalon.data.response.CartsItemResponse
import com.dot_jo.whysalon.databinding.FragmentBasketBinding
import com.dot_jo.whysalon.ui.activity.MainActivity
import com.dot_jo.whysalon.ui.adapter.booking.BasketsAdapter
 import com.dot_jo.whysalon.ui.interfaces.BasketClickListener
import com.dot_jo.whysalon.util.Constants
import com.dot_jo.whysalon.util.ext.hideKeyboard
import com.dot_jo.whysalon.util.ext.init
import com.dot_jo.whysalon.util.ext.roundTo
import com.dot_jo.whysalon.util.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.cardback

@AndroidEntryPoint
class BasketFragment : BaseFragment<FragmentBasketBinding>(), BasketClickListener {
    private var item: CartsItemResponse? = null
    private lateinit var parent: MainActivity
    val mViewModel: BasketViewModel by viewModels()
var total :String? = null
      lateinit var adapter: BasketsAdapter
     override fun onFragmentReady() {

        setupUi()
        onClick()
        initAdapter()

        mViewModel.apply {
            getCart()
            observe(viewState) {
                handleViewState(it)
            }
        }
     /*   binding.swiperefreshHome.setOnRefreshListener {
     mViewModel.            getCart()

            binding.swiperefreshHome.isRefreshing = false
        }*/
    }

    private fun handleViewState(action: BasketAction) {
        when (action) {
            is BasketAction.ShowLoading -> {
                //     binding.shimmer.startShimmerAnimation()
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }


            is BasketAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401") == true) {
                    findNavController().navigate(R.id.loginFirstDialog)
                } else {
                    showToast(action.message)
                }
                showProgress(false)

            }

            is BasketAction.ShowCartData -> {
                showProgress(false)
                //   binding.shimmer.isVisible = false
                //    binding.shimmer.stopShimmerAnimation()
                action.data?.let {
                    loadPackageData(it)
                }
            }
            is BasketAction.DeleteFromCart -> {
                item?.let { adapter.removeItem(it) }
                mViewModel.getCart()
            }
            else -> {}
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun loadPackageData(it: CartResponse) {
     it?.carts?.let {
     //    adapter.differ.submitList( it)
         adapter.ordersList= it
         adapter.notifyDataSetChanged()
     }
        if(it?.carts?.size==0)  {
     binding.lytData.isVisible= false
            binding.lytEmptyState.isVisible= true

        }else{
         binding.lytEmptyState.isVisible= false
         binding.lytData.isVisible= true
         binding.lytNxt.isVisible= true
          total =it?.finalTotal?.toDoubleOrNull()?.roundTo(2).toString()
            binding.tvTotalPrice.setText(total+ resources.getString(R.string.sr))

        }
     }

    private  fun  initAdapter(){
        adapter = BasketsAdapter(this)
        binding.recOffers.init( context, adapter, 2)
 }
    private fun setupUi() {
        parent = requireActivity() as MainActivity
        parent.showBottomBar(false)
        parent.showToolbar(true)
        parent.setTitle(resources.getString(R.string.basket))
        parent.showback(true)
        parent.showNotifactionFragment(false)

        parent.cardback.setOnClickListener {
            activity?.onBackPressed()
        }

    }

    private fun onClick() {
        binding.btnGoservice.setOnClickListener {
            findNavController().navigate(R.id.servicesFragment)
        }
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.chooseBarberFragment , bundleOf(Constants.TOTAL  to total))
        }
        binding.btnAddService.setOnClickListener {
            findNavController().navigate(R.id.servicesFragment   )
        }
    }

    override fun onDeleteClickLisenter(item: CartsItemResponse) {
       this . item = item
        item.id?.let { mViewModel.deleteFromBasket(it) }


    }

}