package com.dot_jo.whysalon.ui.fragment.home

 import android.annotation.SuppressLint
 import android.os.Build
 import android.view.MotionEvent
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
    lateinit var adapter: FilterServiceHomeAdapter
    private lateinit var parent: MainActivity
    private val mViewModel: HomeViewModel by viewModels()

   var list: ArrayList<CategoriesAndServices> = arrayListOf()
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
binding.lytData.isVisible= true
                action.data.categoriesAndServices?.let {
list= it
                    adapter.list = it
                    adapter.notifyDataSetChanged()
                    if(it.get(0).services.isNullOrEmpty()){
                        binding.lytEmptyState.isVisible = true
                        binding.lytData.isVisible = false
                    }else {
                        it?.get(0)?.checked = 1
                        it.get(0)?.services?.let {

                            adapterNewService.list = it
                            adapterNewService.notifyDataSetChanged()

                            current=0
                            serviceCount= it.size
                        }
                    }}
            }

          /*  is HomeAction.PackagesSucess -> {

                showProgress(false)

                action.data.packages?.let {

                    if (it.size > 0) {
                        adapterPackages.list = it
                        adapterPackages.notifyDataSetChanged()
                        binding.rvRecommended.isVisible= true

                    } else {
                        binding.rvRecommended.isVisible= false


                    } }
            }
*/

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
          /*  is HomeAction.ServicesByCategory -> {

                showProgress(false)
                action.data.services?.let {
                    if (it.isNullOrEmpty()) {
                        binding.lytEmptyState.isVisible = true
                        binding.lytData.isVisible = false
                    } else {
                        binding.lytEmptyState.isVisible = false
                        binding.lytData.isVisible = true
                        adapterNewService.list = it
                        adapterNewService.notifyDataSetChanged()
                        binding.rvSubServices.addItemDecoration(SimpleDividerItemDecoration(requireContext()));
                    }
                }
            }*/
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
        adapter = FilterServiceHomeAdapter(this)
        binding.rvServices.init(requireContext(), adapter, 1)

        adapterNewService = NewServiceAdapter(this)
        binding.rvSubServices.init(requireContext(), adapterNewService, 2)
        binding.rvSubServices.addItemDecoration(SimpleDividerItemDecoration(requireContext()));
        binding.rvSubServices.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {

                }
                if (!recyclerView.canScrollVertically(0) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                }

            }

            /*
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val layoutManager = binding.rvSubServices.layoutManager as LinearLayoutManager
        val isScrolledToTop =
            layoutManager.findFirstVisibleItemPosition() == 0 && layoutManager.findViewByPosition(0)?.top == 0
        val isScrolledToBottom =
            layoutManager.findLastVisibleItemPosition() + layoutManager.childCount == layoutManager.itemCount

        if (isScrolledToTop) {
            if (current < list.size - 1) {
                current = current + 1
                list.get(current).checked = 1
                adapter.selectOneItemOnly(list.get(current), current)
                adapter.notifyDataSetChanged()
                adapterNewService.list = list.get(current).services
                adapterNewService.notifyDataSetChanged()
            }
        } else if (isScrolledToBottom) {
            if (current > 1) {
                current = current - 1
                list.get(current).checked = 1
                adapter.selectOneItemOnly(list.get(current), current)
                adapter.notifyDataSetChanged()
                adapterNewService.list = list.get(current).services
                adapterNewService.notifyDataSetChanged()
            }
        }
        else {

            }
            //if (!recyclerView.canScrollVertically(1) && dy > 0)
            */
            /* {
        //    Toast.makeText(requireContext(), "Last", Toast.LENGTH_LONG).show();

            //scrolled to BOTTOM
        }else if (!recyclerView.canScrollVertically(-1) && dy < 0)
        {
                     //scrolled to TOP
        }}*//*

        }
*/
        })

       /* binding.rvSubServices.setOnScrollChangeListener { view, i, i1, i2, i3 ->
            if (!binding.rvSubServices.canScrollVertically(RecyclerView.FOCUS_DOWN)) {
                *//*   *//*


            } else {


            }
        }*/
        val linearLayoutManager = binding.rvSubServices.layoutManager as LinearLayoutManager

/*
        binding.rvSubServices.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                if (e.getAction() == MotionEvent.ACTION_UP
                    || e.getAction() == MotionEvent.ACTION_MOVE){
                    if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() > 0)
                    {
                        if (current > 1) {
                            current = current - 1
                            list.get(current).checked = 1
                            adapter.selectOneItemOnly(list.get(current), current)
                            adapter.notifyDataSetChanged()
                            adapterNewService.list = list.get(current).services
                            adapterNewService.notifyDataSetChanged()
                        }                 }

                    if (linearLayoutManager.findLastVisibleItemPosition()+1 < binding.rvSubServices.getAdapter()?.getItemCount()!!)
                    {
                        if (current < list.size - 1) {
                            current = current + 1
                            list.get(current).checked = 1
                            adapter.selectOneItemOnly(list.get(current), current)
                            adapter.notifyDataSetChanged()
                            adapterNewService.list = list.get(current).services
                            adapterNewService.notifyDataSetChanged()
                        }                          }
                }
                return false;
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
             }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
             }
        })
*/
    }
                /*   fun onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                        } */

/*
// ... ... ...

        // ... ... ...
        fun onScroll(
            lw: AbsListView, firstVisibleItem: Int,
            visibleItemCount: Int, totalItemCount: Int
        ) {
            when (lw.id) {
                R.id.your_list_id -> {

                    // Make your calculation stuff here. You have all your
                    // needed info from the parameters of this function.

                    // Sample calculation to determine if the last
                    // item is fully visible.
                    val lastItem = firstVisibleItem + visibleItemCount
                    if (lastItem == totalItemCount) {
                        if (preLast !== lastItem) {
                            //to avoid multiple calls for last item
                            Log.d("Last", "Last")
                            preLast = lastItem
                        }
                    }
                }
            }
        }
)*/
/*

        adapterCategories = CategoriesAdapter(this)
        binding.recServices.init(requireContext(), adapterCategories, 1)

        adapterOffers = OffersAdapter(this)
        binding.recOffers.init(requireContext(), adapterOffers, 2)
*/

    override fun onFilterOffersByCategory(position: Int, item: CategoriesAndServices?) {
      current   =position
      item?.services?.let {
            if (it.isNullOrEmpty()) {
                binding.lytEmptyState.isVisible = true
                binding.lytData.isVisible = false
            } else {
                binding.lytEmptyState.isVisible = false
                binding.lytData.isVisible = true
                adapterNewService.list = it
                adapterNewService.notifyDataSetChanged()
                binding.rvSubServices.addItemDecoration(SimpleDividerItemDecoration(requireContext()));
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