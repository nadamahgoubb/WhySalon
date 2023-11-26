package com.dot_jo.whysalon.ui.fragment.home

 import android.annotation.SuppressLint
 import android.os.Build
 import androidx.annotation.RequiresApi
 import androidx.core.view.isVisible
 import androidx.fragment.app.viewModels
 import androidx.navigation.fragment.findNavController
 import androidx.recyclerview.widget.LinearLayoutManager
 import androidx.recyclerview.widget.RecyclerView
 import com.dot_jo.whysalon.R
 import com.dot_jo.whysalon.base.BaseFragment
 import com.dot_jo.whysalon.data.PrefsHelper
 import com.dot_jo.whysalon.data.response.CategoriesAndServices
 import com.dot_jo.whysalon.data.response.ServicesInCatgories
 import com.dot_jo.whysalon.databinding.FragmentHomeBinding
 import com.dot_jo.whysalon.ui.activity.MainActivity
 import com.dot_jo.whysalon.ui.adapter.home.FilterServiceHomeAdapter
 import com.dot_jo.whysalon.ui.adapter.home.NewServiceAdapter
 import com.dot_jo.whysalon.ui.interfaces.FilterHomeByServiceClickListener
 import com.dot_jo.whysalon.ui.interfaces.HomeSericeListener
 import com.dot_jo.whysalon.util.SimpleDividerItemDecoration
 import com.dot_jo.whysalon.util.ext.hideKeyboard
 import com.dot_jo.whysalon.util.ext.init
 import com.dot_jo.whysalon.util.observe
 import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() , FilterHomeByServiceClickListener, HomeSericeListener {
var serviceCount=0
    var current =0
   // lateinit var adapterPackages: PackagesAdapter
    lateinit var adapterNewService: NewServiceAdapter
  //  lateinit var adapterOffers: OffersAdapter
    lateinit var adapter_: FilterServiceHomeAdapter
    private lateinit var parent: MainActivity
    private val mViewModel: HomeViewModel by viewModels()

   var list: ArrayList<CategoriesAndServices> = arrayListOf()
   var listSub: ArrayList<ServicesInCatgories> = arrayListOf()
    override fun onFragmentReady() {
        setupUi()
        onClick()
        initAdapter()

        mViewModel.apply {
            getCategoiesAndServices()
           // getPackages()
       //     getOffers()
            getCart()

            observe(viewState) {
                handleViewState(it)
            }
        }
        binding.swiperefreshHome.setOnRefreshListener {
        /*    mViewModel.getCategoiesAndServices()
          //  mViewModel.getPackages()
            mViewModel.getCart()
          //  mViewModel.getOffers()*/
            binding.swiperefreshHome.isRefreshing = false
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleViewState(action: HomeAction) {
        when (action) {
            is HomeAction.ShowLoading -> {

                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }

            is HomeAction.CategoiesAndServicesSucess -> {
                showProgress(false)
                binding.lytData.isVisible = true
                listSub = arrayListOf()
                action.data.categoriesAndServices?.let {
                    list = it
                    adapter_.list = it
                    adapter_.notifyDataSetChanged()

                    it.forEachIndexed { index, element ->

                            element.index = index
                            element.services.forEach {
                                it.index = index
                            }
                            listSub.addAll(element.services)



                    }
                    if (it.get(0).services.isNullOrEmpty()) {
                        binding.lytEmptyState.isVisible = true
                        binding.lytData.isVisible = false
                    } else {
                        it?.get(0)?.checked = 1
                        it.get(0)?.services?.let {

                            adapterNewService.list = listSub
                            adapterNewService.notifyDataSetChanged()

                            current = 0
                            serviceCount = it.size
                        }
                    }
                }

            }

                is HomeAction.AddItemToCart -> {
                    showToast(getString(R.string.added_to_cart_successfully))
                    mViewModel.getCart()
                }

                is HomeAction.ShowCartData -> {
                    parent.setBadge(action.data.carts.size)

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

    }

    private fun setupUi() {
        parent = requireActivity() as MainActivity
        parent.showBottomBar(true)
        parent.showToolbar(true)
        if (PrefsHelper.getUserData() == null) {
            parent.setToolbarTitle(resources.getString(com.dot_jo.whysalon.R.string.hello))
        } else {
            parent.setToolbarTitle(resources.getString(com.dot_jo.whysalon.R.string.hello) + " " + PrefsHelper.getUserData()?.client?.name)

        }
        parent.showback(false)

        parent.showNotifactionFragment(false)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initAdapter() {
        adapter_ = FilterServiceHomeAdapter(this)
        binding.rvServices.init(requireContext(), adapter_, 1)

        adapterNewService = NewServiceAdapter(this)
        binding.rvSubServices.init(requireContext(), adapterNewService, 2)
        //  var linearLayoutManager = LinearLayoutManager(requireContext())
        binding.rvSubServices.apply {
            val linearLayoutManager = binding.rvSubServices.layoutManager as LinearLayoutManager
            //   layoutManager = linearLayoutManager
            //    adapter = CategoryAdapter(catModel)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                    if(isSelectSubCatScrollClick) {
                    linearLayoutManager?.let { lm ->
                        val index = lm.findFirstVisibleItemPosition()
                        if ((listSub.size - 1) >= index) {
                            listSub[index]?.let { sd ->
                             if (current != sd. index) {
                                 current = sd.index
                                 list.get(current).checked = 1
                                 adapter_?.selectOneItemOnly(
                                     list.get(current),
                                     list.get(current).index
                                 )
                                 //  selectOneItemOnly(currentItem , position)
                                 binding.rvServices.layoutManager?.scrollToPosition(current)
                                 //    binding.rvSubServices.layoutManager?.scrollToPosition(current)
                             }    }
                        }
                    }
                 }
            })
        }
    }

    override fun onFilterOffersByCategory(position: Int, item: CategoriesAndServices?) {
      current   =position
        adapter_?.selectOneItemOnly(list.get(current), current)
        binding.rvServices.layoutManager?.scrollToPosition(current)
        item?.services?.let {
            if (it.isNullOrEmpty()) {
                binding.lytEmptyState.isVisible = true
                binding.lytData.isVisible = false
            } else {
                binding.lytEmptyState.isVisible = false
                binding.lytData.isVisible = true
            //    adapterNewService.list = it
              //  adapterNewService.notifyDataSetChanged()
                var newPosition =0
                var  i =0
                while(i   < position){
                    newPosition= newPosition+list.get(i).services.count()
            i++
                }
                binding.rvSubServices.layoutManager?.scrollToPosition( newPosition);
            }
        }  }

    override fun onBookNowClickListener(position: Int, item: ServicesInCatgories) {
        (item as ServicesInCatgories)?.price?.let { it1 ->
            (item as ServicesInCatgories)?.serviceId?.let { it2 ->
                mViewModel.addToBasket(
                    null,
                    it2,
                    it1
                )
            }
        }  }

}