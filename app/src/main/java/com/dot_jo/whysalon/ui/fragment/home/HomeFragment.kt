package com.dot_jo.whysalon.ui.fragment.home

import android.graphics.Paint
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseFragment
import com.dot_jo.whysalon.data.PrefsHelper
import com.dot_jo.whysalon.data.response.CategoriesItem
import com.dot_jo.whysalon.data.response.OfferssItem
import com.dot_jo.whysalon.data.response.ServicesItem
import com.dot_jo.whysalon.databinding.FragmentHomeBinding
import com.dot_jo.whysalon.ui.activity.MainActivity
import com.dot_jo.whysalon.ui.adapter.home.CategoriesAdapter
import com.dot_jo.whysalon.ui.adapter.home.OffersAdapter
import com.dot_jo.whysalon.ui.adapter.home.PackagesAdapter
import com.dot_jo.whysalon.ui.interfaces.HomeClickListener
import com.dot_jo.whysalon.ui.interfaces.OffersClickListener
import com.dot_jo.whysalon.util.Constants
import com.dot_jo.whysalon.util.ext.hideKeyboard
import com.dot_jo.whysalon.util.ext.init
import com.dot_jo.whysalon.util.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(), HomeClickListener, OffersClickListener {

    lateinit var adapterPackages: PackagesAdapter
    lateinit var adapterCategories: CategoriesAdapter
    lateinit var adapterOffers: OffersAdapter
    private lateinit var parent: MainActivity
    private val mViewModel: HomeViewModel by viewModels()


    override fun onFragmentReady() {
        setupUi()
        onClick()
        initAdapter()
        binding.tvServicesViewAll.setPaintFlags(binding.tvServicesViewAll.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        binding.tvOffersViewAll.setPaintFlags(binding.tvOffersViewAll.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        binding.tvRecommendedViewAll.setPaintFlags(binding.tvRecommendedViewAll.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

        mViewModel.apply {
            getCategory()
            getPackages()
            getOffers()

            observe(viewState) {
                handleViewState(it)
            }
        }
        binding.swiperefreshHome.setOnRefreshListener {
            mViewModel.getCategory()
            mViewModel.getPackages()
            mViewModel.getOffers()
            binding.swiperefreshHome.isRefreshing = false
        }
    }
    private fun handleViewState(action: HomeAction) {
        when (action) {
            is HomeAction.ShowLoading -> {
                binding.shimmer.startShimmerAnimation()
                binding.shimmerPackages.startShimmerAnimation()
                binding.shimmerOffers.startShimmerAnimation()
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }

            is HomeAction.CategoriesSucess -> {
                showProgress(false)
                binding.shimmer.isVisible = false
                binding.shimmer.stopShimmerAnimation()
                action.data.services?.let {
                    adapterCategories.list = it
                    adapterCategories.notifyDataSetChanged()
                }
            }

            is HomeAction.PackagesSucess -> {
                binding.shimmerPackages.isVisible = false
                binding.shimmerPackages.stopShimmerAnimation()
                showProgress(false)

                action.data.packages?.let {
                    adapterPackages.list = it
                    adapterPackages.notifyDataSetChanged()
                }
            }   is HomeAction.ShowOffers -> {
            binding.shimmerOffers.isVisible = false
            binding.shimmerOffers.stopShimmerAnimation()
            showProgress(false)

            action.data.offers?.let {
                adapterOffers.list = it
                adapterOffers.notifyDataSetChanged()
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

    private fun onClick() {
        binding.tvServicesViewAll.setOnClickListener {
            findNavController().navigate(R.id.servicesFragment)
        }
        binding.tvRecommendedViewAll.setOnClickListener {
            findNavController().navigate(R.id.packagesFragment)
        }
        binding.tvOffersViewAll.setOnClickListener {
            findNavController().navigate(R.id.offersFragment)
        }

    }

    private fun setupUi() {
        parent = requireActivity() as MainActivity
        parent.showBottomBar(true)
        parent.showToolbar(true)
        if (PrefsHelper.getUserData() == null) {
            parent.setTitle(resources.getString(R.string.hello))
        } else {
            parent.setTitle(resources.getString(R.string.hello) + " " + PrefsHelper.getUserData()?.client?.name)

        }
        parent.showback(false)

        parent.showNotifactionFragment(false)

    }

    private fun initAdapter() {
        adapterPackages = PackagesAdapter(this)
        binding.rvRecommended.init(requireContext(), adapterPackages, 1)

        adapterCategories = CategoriesAdapter(this)
        binding.recServices.init(requireContext(), adapterCategories, 1)

        adapterOffers = OffersAdapter(this)
        binding.recOffers.init(requireContext(), adapterOffers, 2)

    }


    override fun onCategoryClickListener(item: CategoriesItem) {
        // findNavController().navigate(R.id.itemDetailsFragment, bundleOf(Constants.SERVICE to item))
        findNavController().navigate(R.id.subServiceFragment, bundleOf(Constants.SERVICE to item))
    }

    override fun onPackagesClickListener(item: ServicesItem) {
        findNavController().navigate(
            R.id.itemDetailsFragment, bundleOf(
                Constants.PACKAGE to item, Constants.Type to Constants.Package
            )
        )
    }

    override fun onOffersClickListener(item: OfferssItem) {
        findNavController().navigate(
            R.id.itemDetailsFragment, bundleOf(
                Constants.PACKAGE to item, Constants.Type to Constants.OFFERS
            )
        )  }

}