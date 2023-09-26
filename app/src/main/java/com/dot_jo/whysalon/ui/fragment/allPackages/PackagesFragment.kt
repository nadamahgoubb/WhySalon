package com.dot_jo.whysalon.ui.fragment.allPackages

import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseFragment
import com.dot_jo.whysalon.data.response.CategoriesItem
import com.dot_jo.whysalon.data.response.ServicesItem
import com.dot_jo.whysalon.databinding.FragmentPackagesBinding
import com.dot_jo.whysalon.ui.activity.MainActivity
import com.dot_jo.whysalon.ui.adapter.packagee.PackagesScreenAdapter
import com.dot_jo.whysalon.ui.fragment.home.HomeAction
import com.dot_jo.whysalon.ui.fragment.home.HomeViewModel
import com.dot_jo.whysalon.ui.interfaces.HomeClickListener
import com.dot_jo.whysalon.util.Constants
import com.dot_jo.whysalon.util.ext.hideKeyboard
import com.dot_jo.whysalon.util.ext.init
import com.dot_jo.whysalon.util.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.cardback

@AndroidEntryPoint
class PackagesFragment : BaseFragment<FragmentPackagesBinding>(), HomeClickListener {
    private lateinit var parent: MainActivity
    private val mViewModel: HomeViewModel by viewModels()
    lateinit var adapterPackages: PackagesScreenAdapter
    override fun onFragmentReady() {
        setupUi()
        initAdapter()
        mViewModel.apply {
            getPackages()
            observe(viewState) {
                handleViewState(it)
            }
        }
        binding.swiperefreshHome.setOnRefreshListener {
            mViewModel.getPackages()
            binding.swiperefreshHome.isRefreshing = false
        }


    }

    private fun handleViewState(action: HomeAction) {
        when (action) {
            is HomeAction.ShowLoading -> {
                binding.shimmer.startShimmerAnimation()
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }

            is HomeAction.PackagesSucess -> {
                binding.shimmer.isVisible = false
                binding.shimmer.stopShimmerAnimation()
                showProgress(false)

                action.data.packages?.let {
                    adapterPackages.list = it
                    adapterPackages.notifyDataSetChanged()
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
        parent.showToolbar(true)
        parent.setToolbarTitle(resources.getString(R.string.packages))
        parent.showback(true)
        parent.showNotifactionFragment(false)
        parent.cardback.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun initAdapter() {
        adapterPackages = PackagesScreenAdapter(this)
        binding.rvPackages.init(requireContext(), adapterPackages, 2)

    }

    override fun onCategoryClickListener(item: CategoriesItem) {
    }

    override fun onPackagesClickListener(item: ServicesItem) {
        findNavController().navigate(
            R.id.itemDetailsFragment, bundleOf(
                Constants.PACKAGE to item, Constants.Type to Constants.Package
            )
        )

    }

    override fun onBookNowClickListener(item: ServicesItem) {
        TODO("Not yet implemented")
    }

}