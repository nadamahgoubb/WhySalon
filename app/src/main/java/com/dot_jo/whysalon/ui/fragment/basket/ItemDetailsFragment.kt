package com.dot_jo.whysalon.ui.fragment.basket

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseFragment
import com.dot_jo.whysalon.data.PrefsHelper
import com.dot_jo.whysalon.data.response.CategoriesItem
import com.dot_jo.whysalon.data.response.ImageItem
import com.dot_jo.whysalon.data.response.OfferssItem
import com.dot_jo.whysalon.data.response.ServicesItem
import com.dot_jo.whysalon.databinding.FragmentItemDetailsBinding
import com.dot_jo.whysalon.ui.activity.MainActivity
import com.dot_jo.whysalon.ui.adapter.packagee.PackagesServicesLineAdapter
import com.dot_jo.whysalon.ui.fragment.allPackages.SectionsHomePagerAdapter
import com.dot_jo.whysalon.util.Constants
import com.dot_jo.whysalon.util.ext.hideKeyboard
import com.dot_jo.whysalon.util.ext.init
import com.dot_jo.whysalon.util.ext.isNull
import com.dot_jo.whysalon.util.getDuration
import com.dot_jo.whysalon.util.observe
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.cardback
import kotlin.math.roundToInt

//OFFERS
@AndroidEntryPoint
class ItemDetailsFragment : BaseFragment<FragmentItemDetailsBinding>() {
    val mViewModel: BasketViewModel by viewModels()
    private lateinit var parent: MainActivity
    lateinit var adapter: PackagesServicesLineAdapter
    var item: Any? = null
    var type = -1
    override fun onFragmentReady() {
        setupUi()
        onClick()
        initAdapter()

        mViewModel.apply {
            observe(viewState) {
                handleViewState(it)
            }
        }
        binding.swiperefreshHome.setOnRefreshListener {
            if (type == Constants.Package) {
                (item as ServicesItem).id?.let { mViewModel.getPackagesDetails(it) }
            } else if (type == Constants.Service) {
                (item as CategoriesItem).id?.let { mViewModel.getServiceDetails(it) }

            } else { //OFFERS
            }
            binding.swiperefreshHome.isRefreshing = false
        }
    }

    private fun handleViewState(action: BasketAction) {
        when (action) {
            is BasketAction.ShowLoading -> {
                //     binding.shimmer.startShimmerAnimation()
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }


            is BasketAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401") == true) {
                    findNavController().navigate(R.id.loginFirstDialog)
                } else {
                    showToast(action.message)
                }
                showProgress(false)

            }

            is BasketAction.ShowServiceDetails -> {
                showProgress(false)
                //   binding.shimmer.isVisible = false
                //    binding.shimmer.stopShimmerAnimation()
                action.data.service?.let {
                    loadPackageData(it)
                }
            }

            is BasketAction.ShowPackageDetails -> {
                showProgress(false)
                //   binding.shimmer.isVisible = false
                //  binding.shimmer.stopShimmerAnimation()
                action.data.packagee?.let {
                    loadPackageData(it)
                }
                loadPackageServices(action.data.services)

            }

            is BasketAction.AddItemToCart ->

                //      findNavController().navigate(R.id.basketFragment)
                showToast(action.data.scalar)


            else -> {}
        }
    }


    private fun setupUi() {
        parent = requireActivity() as MainActivity
        parent.showBottomBar(false)
        parent.showToolbar(false)
        parent.showback(true)

        parent.cardback.setOnClickListener {
            activity?.onBackPressed()
        }
        type = arguments?.getInt(Constants.Type)!!
        if (type == Constants.Package) {
            binding.tv1.setText(resources.getString(R.string.about_this_pack))
            item = arguments?.getParcelable(Constants.PACKAGE)
            (item as ServicesItem)?.id?.let { mViewModel.getPackagesDetails(it) }

        } else if (type == Constants.OFFERS) {

            binding.tv1.setText(resources.getString(R.string.about_this_offer))
            item = arguments?.getParcelable(Constants.PACKAGE) //!! as OfferssItem
            loadOfferData(item as OfferssItem)

        } else {
            binding.tv1.setText(resources.getString(R.string.about_this_service))
            item = arguments?.getParcelable(Constants.PACKAGE)
            (item as ServicesItem)?.id?.let { mViewModel.getServiceDetails(it) }

        }

    }

    @SuppressLint("SetTextI18n")
    private fun loadOfferData(item: OfferssItem) {
        binding.lytData.isVisible = true
         binding.tvDesc.isVisible =true
        binding.tvTitle.setText(item?.services?.name.toString())
        binding.tvDesc.setText(item?.services?.description.toString())
        binding.tvMoneyTitle.text =
            resources.getString(R.string.price_from) + " " + item.price?.toDoubleOrNull()
                ?.roundToInt()
                .toString() + " " + resources.getString(R.string.sr)
        // binding.tvTime.setText(item?.services?.duration.toString() + " " + resources.getString(R.string.min))
        binding.tvTime.setText(item?.services?.duration?.toIntOrNull()
            ?.let { getDuration(it, requireContext()) })
        item?.services?.images?.let { loadSlider(it) }

    }


    private fun initAdapter() {
        adapter = PackagesServicesLineAdapter()
        binding.rvServices.init(requireContext(), adapter, 2)
    }

    private fun onClick() {
        binding.lytNxt.setOnClickListener {
            if (PrefsHelper.getUserData().isNull()) {
                findNavController().navigate(R.id.loginFirstDialog)

            } else {
                //
                //     if (type == Constants.Package) {
                //         //          //    loadPackageData(item as ServicesItem)
                //     } else {
                //         item?.price?.let { it1 -> item?.id?.let { it2 -> mViewModel.addToBasket(null, it2,it1) } }
                //         //   loadPackageData(item as CategoriesItem)
//
                //     }
                if (type == Constants.Package) {
                    (item as ServicesItem)?.price?.let { it1 ->
                        (item as ServicesItem)?.id?.let { it2 ->
                            mViewModel.addToBasket(
                                it2,
                                null,
                                it1
                            )
                        }
                    }

                    //    loadPackageData(item as ServicesItem)
                } else if (type == Constants.OFFERS) {
                    //!! as OfferssItem
                    (item as OfferssItem)?.price?.let { it1 ->
                        (item as OfferssItem)?.service_id?.let { it2 ->
                            mViewModel.addToBasket(
                                null,
                                it2,
                                it1
                            )
                        }
                    }


                } else {
                    (item as ServicesItem)?.price?.let { it1 ->
                        (item as ServicesItem)?.id?.let { it2 ->
                            mViewModel.addToBasket(
                                null,
                                it2,
                                it1
                            )
                        }
                    }

                }
            }

        }
        binding.cardback.setOnClickListener {

            findNavController().popBackStack()
        }

        binding.cardNotify.setOnClickListener {

            findNavController().navigate(R.id.notifactionFragment)
        }
    }

    fun loadPackageData(item: ServicesItem?) {
        binding.lytData.isVisible = true
        binding.tvServiceCount.isVisible =true
        binding.tvTitle.setText(item?.name.toString())
        binding.tvMoneyTitle.setText(
            resources.getString(R.string.price_from) + item?.price?.toDoubleOrNull()?.roundToInt()
                .toString() + resources.getString(R.string.sr)
        )
        //     binding.tvTime.setText(item?.duration.toString() + " " + resources.getString(R.string.min))
        binding.tvTime.setText(item?.duration?.toIntOrNull()
            ?.let { getDuration(it, requireContext()) })


        item?.images?.let { loadSlider(it) }

        if (type == Constants.Package) {
            binding.tvServiceCount.setText(item?.servicesCount.toString() + resources.getString(R.string.services_) + ":")

        } else {
            binding.tvDesc.setText(item?.description.toString())
            binding.tvDesc.isVisible =true
        }
    }


    private fun loadPackageServices(services: ArrayList<ServicesItem>) {
        adapter.list = services
        adapter.notifyDataSetChanged()
    }


    private lateinit var handler: Handler
    private var urls: MutableList<ImageItem> = mutableListOf()
    private fun loadSlider(mainSliders: ArrayList<ImageItem>) {
        handler = Handler(Looper.myLooper()!!)
        if (mainSliders != null) {
            urls = mainSliders
            val adapter = activity?.let { SectionsHomePagerAdapter(binding.viewpager, mainSliders) }
            binding.viewpager.adapter = adapter
            binding.viewpager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    handler.removeCallbacks(runnable)
                    handler.postDelayed(runnable, 4000)
                }
            })
        }
        binding.tabLayout.setupWithViewPager(binding.viewpager)
    }

    fun TabLayout.setupWithViewPager(viewPager: ViewPager2) {
        TabLayoutMediator(binding.tabLayout, viewPager) { tab, position ->
        }.attach()

    }

    override fun onPause() {
        super.onPause()
        try {
            handler.removeCallbacks(runnable)

        } catch (e: Exception) {

        }
    }

    override fun onResume() {
        super.onResume()
        try {
            handler.postDelayed(runnable, 4000)

        } catch (e: Exception) {

        }
    }

    private val runnable = Runnable {
        if (binding.viewpager.currentItem < (urls.size - 1)) {
            binding.viewpager.currentItem = binding.viewpager.currentItem + 1

        } else {
            binding.viewpager.currentItem = 0

        }
    }

}