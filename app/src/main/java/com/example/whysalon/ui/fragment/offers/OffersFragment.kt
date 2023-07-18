package com.example.whysalon.ui.fragment.offers

 import androidx.navigation.fragment.findNavController
 import com.example.whysalon.R
 import com.example.whysalon.base.BaseFragment
 import com.example.whysalon.data.PrefsHelper
 import com.example.whysalon.databinding.FragmentOffersBinding
 import com.example.whysalon.ui.activity.MainActivity
 import com.example.whysalon.ui.adapter.FilterOfferAdapter
 import com.example.whysalon.util.ext.init
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OffersFragment : BaseFragment<FragmentOffersBinding>() {
    lateinit var adapter: FilterOfferAdapter
    private lateinit var parent: MainActivity

    override fun onFragmentReady() {
        setupUi()
        onClick()
        initAdapter( )
        loadData()

}

    private fun onClick() {
        binding.item11.root.setOnClickListener {
            if (PrefsHelper.getToken().isNullOrEmpty()) {
                findNavController().navigate(R.id.loginFirstDialog)

             } else {
                findNavController().navigate(R.id.calenderFragment)
            }
         }
        binding.item12.root.setOnClickListener {
            if (PrefsHelper.getToken().isNullOrEmpty()) {
                findNavController().navigate(R.id.loginFirstDialog)

            } else {
                findNavController().navigate(R.id.calenderFragment)
            }        }
    }
    private fun setupUi() {
        parent = requireActivity() as MainActivity
        parent.showBottomBar(true)
        parent.showToolbar(false)

    }
private fun loadData() {
     adapter.list= listOf("All", "Hair Service","Beard Service","Facial Services","Packages" ) as MutableList<String>
    adapter.notifyDataSetChanged()
}
    private fun initAdapter() {
        adapter = FilterOfferAdapter( )
        binding.rvFilters.init(requireContext(), adapter, 1)

    }
}