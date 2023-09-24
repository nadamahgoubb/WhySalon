package com.dot_jo.whysalon.ui.fragment.offers

import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseFragment
import com.dot_jo.whysalon.data.response.CategoriesItem
import com.dot_jo.whysalon.data.response.OfferssItem
import com.dot_jo.whysalon.databinding.FragmentOffersBinding
import com.dot_jo.whysalon.ui.activity.MainActivity
import com.dot_jo.whysalon.ui.adapter.FilterOfferAdapter
import com.dot_jo.whysalon.ui.adapter.home.OffersAdapter
import com.dot_jo.whysalon.ui.fragment.home.HomeAction
import com.dot_jo.whysalon.ui.fragment.home.HomeViewModel
import com.dot_jo.whysalon.ui.interfaces.FilterOffersByCategoryClickListener
import com.dot_jo.whysalon.ui.interfaces.OffersClickListener
import com.dot_jo.whysalon.util.Constants
import com.dot_jo.whysalon.util.ext.hideKeyboard
import com.dot_jo.whysalon.util.ext.init
import com.dot_jo.whysalon.util.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OffersFragment : BaseFragment<FragmentOffersBinding>(), OffersClickListener,
    FilterOffersByCategoryClickListener {
    lateinit var adapter: FilterOfferAdapter
    private lateinit var parent: MainActivity
    lateinit var adapterOffers: OffersAdapter
    private val mViewModel: HomeViewModel by viewModels()

    override fun onFragmentReady() {
        setupUi()
         initAdapter()

         mViewModel.apply {
            getOffers()
            getCategory()
            observe(viewState) {
                handleViewState(it)
            }
        }
        binding.swiperefreshHome.setOnRefreshListener {

            mViewModel.getCategory()
            mViewModel.getOffers()
            binding.swiperefreshHome.isRefreshing = false
        }

    }

    private fun handleViewState(action: HomeAction) {
        when (action) {
            is HomeAction.ShowLoading -> {

                binding.shimmerOffers.startShimmerAnimation()
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }

            is HomeAction.ShowOffers -> {
                binding.shimmerOffers.isVisible = false
                binding.shimmerOffers.stopShimmerAnimation()
                showProgress(false)
                action.data.offers?.let {
                    if (it.isNullOrEmpty()) {
                        binding.lytEmptyState.isVisible = true
                        binding.lytData.isVisible = false
                    } else {
                        binding.lytEmptyState.isVisible = false
                        binding.lytData.isVisible = true
                        adapterOffers.list = it
                        adapterOffers.notifyDataSetChanged()
                    }
                }
            }
            is HomeAction.CategoriesSucess -> {
                showProgress(false)

                action.data.services?.let {
                    adapter.list = it
                    adapter.notifyDataSetChanged()
                }
            }

            is HomeAction.ShowFailureMsg -> action.message?.let {
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
        parent.showback(true)
        parent.showToolbar(false)
 binding.card.setOnClickListener {
    findNavController().navigate(R.id.notifactionFragment)
}
    }

    private fun initAdapter() {
        adapter = FilterOfferAdapter(this)
        binding.rvFilters.init(requireContext(), adapter, 1)

        adapterOffers = OffersAdapter(this)
        binding.rvOffers.init(requireContext(), adapterOffers, 2)

    }

    override fun onOffersClickListener(item: OfferssItem) {
        findNavController().navigate(
            R.id.itemDetailsFragment, bundleOf(
                Constants.PACKAGE to item, Constants.Type to Constants.OFFERS
            )
        )
     }

    override fun onFilterOffersByCategory(item: CategoriesItem?) {
        item?.id?.let { mViewModel.getOffers(it) }
    }

}