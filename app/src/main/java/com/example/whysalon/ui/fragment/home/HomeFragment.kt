package com.example.whysalon.ui.fragment.home

import android.graphics.Paint
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.whysalon.R
import com.example.whysalon.base.BaseFragment
import com.example.whysalon.data.response.ServiceItem
import com.example.whysalon.databinding.FragmentHomeBinding
import com.example.whysalon.ui.activity.MainActivity
import com.example.whysalon.ui.adapter.RecommendationAdapter
import com.example.whysalon.ui.interfaces.HistoryClickListener
import com.example.whysalon.util.ext.init
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
@AndroidEntryPoint

class HomeFragment : BaseFragment<FragmentHomeBinding>(), HistoryClickListener {

    lateinit var adapter: RecommendationAdapter
    private lateinit var parent: MainActivity

    override fun onFragmentReady() {
        setupUi()
        onClick()
        initAdapter()
        binding.tvServicesViewAll.setPaintFlags(binding.tvServicesViewAll.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        binding.tvOffersViewAll.setPaintFlags(binding.tvOffersViewAll.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        binding.item12.tvMoneyBefore.setPaintFlags(binding.item12.tvMoneyBefore.getPaintFlags() or Paint.LINEAR_TEXT_FLAG )
        lifecycleScope.launch{
            delay(1000)
            loadData()
        }
    }

    private fun onClick() {
        binding.tvServicesViewAll.setOnClickListener {
            findNavController().navigate(R.id.rateBottomSheet)
        }
        binding.tvOffersViewAll.setOnClickListener {
            findNavController().navigate(R.id.offersFragment)
        }
        binding.item12.root.setOnClickListener {
            findNavController().navigate(R.id.calenderFragment)
        }
        binding.item11.root.setOnClickListener {
            findNavController().navigate(R.id.calenderFragment)
        }
        binding.item1.root.setOnClickListener {
            findNavController().navigate(R.id.serviceDetailsFragment)
        }
        binding.item2.root.setOnClickListener {
            findNavController().navigate(R.id.serviceDetailsFragment)
        }
    }

    private fun setupUi() {
        parent = requireActivity() as MainActivity
        parent.showBottomBar(true)
        parent.showToolbar(true)
        parent.setTitle("Hello Ali")
        parent.showback(false)

        parent.showNotifactionFragment(false)

    }
    private fun loadData() {
        var item =ServiceItem(resources.getDrawable(R.drawable.barber_4),"The Barber Name here","Service type:Long hair cut","150 SAR")
        adapter.list= listOf(item, item,item,item,item,item,) as MutableList<ServiceItem>
        adapter.notifyDataSetChanged()
    }
    private fun initAdapter() {
        adapter = RecommendationAdapter( this)
        binding.rvRecommended.init(requireContext(), adapter, 1)

    }

    override fun onHistoryClickListener() {
        findNavController().navigate(R.id.calenderFragment)
    }

}