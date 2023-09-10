package com.dot_jo.whysalon.ui.fragment.notifaction


import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseFragment
import com.dot_jo.whysalon.databinding.FragmentNotifactionBinding
import com.dot_jo.whysalon.ui.activity.MainActivity
import com.dot_jo.whysalon.ui.adapter.NotificationsAdapter
import com.dot_jo.whysalon.ui.adapter.home.CategoriesAdapter
import com.dot_jo.whysalon.ui.adapter.home.OffersAdapter
import com.dot_jo.whysalon.ui.adapter.home.PackagesAdapter
import com.dot_jo.whysalon.ui.fragment.profile.ProfileAction
import com.dot_jo.whysalon.ui.fragment.profile.ProfileViewModel
import com.dot_jo.whysalon.util.ext.hideKeyboard
import com.dot_jo.whysalon.util.ext.init
import com.dot_jo.whysalon.util.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class NotifactionFragment : BaseFragment<FragmentNotifactionBinding>() {

    private lateinit var parent: MainActivity
    private val mViewModel: ProfileViewModel by viewModels()
    private lateinit var notificationsAdapter: NotificationsAdapter
    override fun onFragmentReady() {
        setupUi()
        initAdapter()
        mViewModel.apply {
            mViewModel.getNotifaction()
            observe(viewState) {
                handleViewState(it)
            }
        }
    }

    private fun setupUi() {
        parent = requireActivity() as MainActivity
        parent.showBottomBar(false)
        parent.showToolbar(true)
        parent.showNotifactionFragment(true)

        parent.cardback.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.swiperefreshHome.setOnRefreshListener {
            mViewModel.getNotifaction()

            binding.swiperefreshHome.isRefreshing = false
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
                if (it.contains("401")) {
                    findNavController().navigate(R.id.loginFirstDialog)
                } else {
                    showToast(action.message)
                }
                showProgress(false)

            }

            is ProfileAction.getNotifaction -> {
                notificationsAdapter.list = action.data.notifications!!.data!!
            }

            else -> {

            }
        }
    }

    private fun initAdapter() {
        notificationsAdapter = NotificationsAdapter()
        binding.recNotifications.init(requireContext(), notificationsAdapter, 2)
    }

}