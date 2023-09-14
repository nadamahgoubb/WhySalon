package com.dot_jo.whysalon.ui.fragment.notifaction

import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseFragment
import com.dot_jo.whysalon.data.response.NotificationItem
import com.dot_jo.whysalon.databinding.FragmentNotifactionBinding
import com.dot_jo.whysalon.ui.activity.MainActivity
import com.dot_jo.whysalon.ui.adapter.NotifactionItem
import com.dot_jo.whysalon.ui.adapter.NotifactionsPagingAdapter
import com.dot_jo.whysalon.util.convertPttern
import com.dot_jo.whysalon.util.ext.hideKeyboard
import com.dot_jo.whysalon.util.ext.init
import com.dot_jo.whysalon.util.observe
 import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotifactionFragment : BaseFragment<FragmentNotifactionBinding>() {

    private lateinit var parent: MainActivity
    private val mViewModel: NotifactionViewModel by viewModels()
     private lateinit var adapter: NotifactionsPagingAdapter
     var firstTime = true
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

    }
    private fun handleViewState(action: NotifactionAction) {
        when (action) {
            is NotifactionAction.ShowLoading -> {
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }

            is NotifactionAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401")) {
                    findNavController().navigate(R.id.loginFirstDialog)
                } else {
                    showToast(action.message)
                }
                showProgress(false)

            }

            is NotifactionAction.ShowAllNotifaction -> {
                showProgress(false)

                lifecycleScope.launchWhenStarted {
                    adapter.submitData(action.data)
                    loadData()


                }
            }


            is NotifactionAction.ShowFailureMsg -> action.message?.let {
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

    private fun loadData() {
        var map  = (adapter.snapshot().items as ArrayList<NotificationItem>). groupBy {
            it.created_at?.let { it1 -> convertPttern(it1) }
        } as Map<String, ArrayList<NotificationItem>>

        val myItems: ArrayList<NotifactionItem> = arrayListOf()
        map.forEach {
            myItems.add(NotifactionItem.HeaderItem(it.key))
            myItems.add(NotifactionItem.BodyItem(map.getValue(it.key).get(0)))
        }

        adapter.list = myItems as MutableList<NotifactionItem>
        adapter.mapp = map as MutableMap<String, ArrayList<NotifactionItem>>
        adapter.title = true
        adapter.notifyDataSetChanged()

    }

    private fun initAdapter() {


        adapter = NotifactionsPagingAdapter()
        binding?.rvNotifactions?.init(requireContext(), adapter, 2)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow.collect {
                    binding.preProg.isVisible = it.source.prepend is LoadState.Loading
                    binding.appendProgress.isVisible = it.source.append is LoadState.Loading
                }
            }
        }
        adapter.addLoadStateListener { loadState ->

            // show empty list
            if (loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading) {


            }
            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached) {

                if (adapter.snapshot().items.size == 0) {
                    binding.lytData.visibility = View.GONE
                    binding.lyrEmptyState.visibility = View.VISIBLE
                } else {
                    binding.lyrEmptyState.visibility = View.GONE
                    binding.lytData.visibility = View.VISIBLE
                 if(firstTime){
                     firstTime =false
                     loadData()
                 }
                }

            } else {

                // If we have an error, show a toast*/
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    /* if (it.error.message.equals(Constants.UNAUTHURAIZED_ACCESS)) {
                         showEmptyState(true)
                     } else*/
                    Toast.makeText(activity, it.error.message.toString(), Toast.LENGTH_LONG)
                        .show()
                }

            }
        }


    }

}