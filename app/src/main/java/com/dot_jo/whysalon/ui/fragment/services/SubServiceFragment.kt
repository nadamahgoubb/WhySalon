package com.dot_jo.whysalon.ui.fragment.services


import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseFragment
import com.dot_jo.whysalon.data.response.CategoriesItem
import com.dot_jo.whysalon.data.response.ServicesItem
import com.dot_jo.whysalon.databinding.FragmentSubServiceBinding
import com.dot_jo.whysalon.ui.activity.MainActivity
import com.dot_jo.whysalon.ui.adapter.services.SubServicesAdapter
import com.dot_jo.whysalon.ui.fragment.home.HomeAction
import com.dot_jo.whysalon.ui.fragment.home.HomeViewModel
import com.dot_jo.whysalon.ui.interfaces.HomeClickListener
import com.dot_jo.whysalon.util.Constants
import com.dot_jo.whysalon.util.ext.hideKeyboard
import com.dot_jo.whysalon.util.ext.init
import com.dot_jo.whysalon.util.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class SubServiceFragment : BaseFragment<FragmentSubServiceBinding>(), HomeClickListener {
    private lateinit var parent: MainActivity
    var item: CategoriesItem? = null
    lateinit var adapter: SubServicesAdapter
    private val mViewModel: HomeViewModel by viewModels()
    override fun onFragmentReady() {
        item = arguments?.getParcelable(Constants.SERVICE)
        setupUi()
        initAdapter()

        mViewModel.apply {
            item?.id?.let { it1 -> mViewModel.getServicesInCategory(it1) }

            observe(viewState) {
                handleViewState(it)
            }
        }
        binding.swiperefreshHome.setOnRefreshListener {
            item?.id?.let { it1 -> mViewModel.getServicesInCategory(it1) }
            binding.swiperefreshHome.isRefreshing = false
        }
    }

    private fun initAdapter() {
        adapter = SubServicesAdapter(this)
        binding.recOffers.init(requireContext(), adapter, 2)/*
                   */

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

            is HomeAction.ServicesByCategory -> {
                binding.shimmer.isVisible = false
                binding.shimmer.stopShimmerAnimation()
                showProgress(false)
                action.data.services?.let {
                    if (it.isNullOrEmpty()) {
                        binding.lytEmptyState.isVisible = true
                        binding.lytData.isVisible = false
                    } else {
                        binding.lytEmptyState.isVisible = false
                        binding.lytData.isVisible = true
                        adapter.list = it
                        adapter.notifyDataSetChanged()
                    }
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
        parent.setTitle(item?.name)
        parent.showback(true)

        parent.cardback.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onCategoryClickListener(item: CategoriesItem) {
        // no need
    }

    override fun onPackagesClickListener(item: ServicesItem) {

        findNavController().navigate(
            R.id.itemDetailsFragment, bundleOf(
                Constants.PACKAGE to item, Constants.Type to Constants.Service
            )
        )

    }

    override fun onBookNowClickListener(item: ServicesItem) {

    }

}