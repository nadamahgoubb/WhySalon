package com.example.whysalon.ui.fragment.history

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.whysalon.R
import com.example.whysalon.base.BaseFragment
import com.example.whysalon.data.response.ServiceItem
 import com.example.whysalon.databinding.FragmentHistoryBinding
import com.example.whysalon.ui.activity.MainActivity
import com.example.whysalon.ui.adapter.HistoryAdapter
import com.example.whysalon.ui.adapter.RecommendationAdapter
import com.example.whysalon.ui.interfaces.HistoryClickListener
import com.example.whysalon.util.ext.init
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding>(), HistoryClickListener {


    lateinit var adapter: HistoryAdapter
    private lateinit var parent: MainActivity

    override fun onFragmentReady() {
            setupUi()
            initAdapter()
             lifecycleScope.launch{
                delay(1000)
                loadData()
            }
        }
        private fun setupUi() {
            parent = requireActivity() as MainActivity
            parent.showBottomBar(false)
            parent.showToolbar(true)
            parent.setTitle(resources.getString(R.string.my_history))

            parent.showNotifactionFragment(false)

            parent.cardback.setOnClickListener {
                activity?.onBackPressed()
            }
        }
    private fun loadData() {
        var item = ServiceItem(resources.getDrawable(R.drawable.barber_4),"The Barber Name here","Service type:Long hair cut","150 SAR")
        adapter.list= listOf(item, item,item,item,item,item,) as MutableList<ServiceItem>
        adapter.notifyDataSetChanged()


    }
    private fun initAdapter() {
        adapter = HistoryAdapter(this )
        binding.rvHistory.init(requireContext(), adapter, 1)
        binding.rvHistoryYearBfore.init(requireContext(), adapter, 1)

    }

    override fun onHistoryClickListener() {
         findNavController().navigate(R.id.calenderFragment)
    }

}