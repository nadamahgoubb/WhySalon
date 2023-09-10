package com.dot_jo.whysalon.ui.fragment.services

import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseFragment
import com.dot_jo.whysalon.data.response.CategoriesItem
import com.dot_jo.whysalon.data.response.ServicesItem
import com.dot_jo.whysalon.databinding.FragmentServicesBinding
import com.dot_jo.whysalon.ui.activity.MainActivity
import com.dot_jo.whysalon.ui.adapter.ServicesAdapter
import com.dot_jo.whysalon.ui.fragment.home.HomeAction
import com.dot_jo.whysalon.ui.fragment.home.HomeViewModel
import com.dot_jo.whysalon.ui.interfaces.HomeClickListener
import com.dot_jo.whysalon.util.Constants
import com.dot_jo.whysalon.util.ext.hideKeyboard
import com.dot_jo.whysalon.util.ext.init
import com.dot_jo.whysalon.util.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServicesFragment : BaseFragment<FragmentServicesBinding>(), HomeClickListener {
    private lateinit var parent: MainActivity

     lateinit var adapter: ServicesAdapter
     private val mViewModel: HomeViewModel by viewModels()

    private fun handleViewState(action: HomeAction) {
        when (action) {
            is HomeAction.ShowLoading -> {
                binding.shimmer.startShimmerAnimation()
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
                    adapter.list =it
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
    override fun onFragmentReady() {
        setupUi()
         initAdapter()

        mViewModel.apply {
            getCategory()
             observe(viewState) {
                handleViewState(it)
            }
        }
        binding.swiperefreshHome.setOnRefreshListener {
            mViewModel. getCategory()
             binding.swiperefreshHome.isRefreshing= false
        }
    }

    private fun initAdapter() {
        adapter = ServicesAdapter( this)
        binding.rvService.init(requireContext(), adapter, 2)

    }

    private fun setupUi() {
        parent = requireActivity() as MainActivity
        parent.showBottomBar(true)
        parent.showToolbar(true)
        parent.setTitle(resources.getString(R.string.services))
        parent.showback(false)
        parent.showNotifactionFragment(false)

    }

    override fun onCategoryClickListener(item: CategoriesItem) {
        findNavController().navigate(R.id.subServiceFragment, bundleOf(Constants.SERVICE to item))
    }

    override fun onPackagesClickListener(item: ServicesItem) {
    }

    override fun onBookNowClickListener(item: ServicesItem) {

    }
}