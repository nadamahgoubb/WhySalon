package com.example.whysalon.ui.fragment.allPackages

 import android.os.Handler
import android.os.Looper
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.whysalon.R
import com.example.whysalon.base.BaseFragment
import com.example.whysalon.data.PrefsHelper
import com.example.whysalon.databinding.FragmentItemDetailsBinding
import com.example.whysalon.ui.activity.MainActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.cardback

class ItemDetailsFragment : BaseFragment<FragmentItemDetailsBinding>() {
    private lateinit var parent: MainActivity

    private fun setupUi() {
        parent = requireActivity() as MainActivity
        parent.showBottomBar(false)
        parent.showToolbar(false)
        parent.setTitle(resources.getString(R.string.hair_services))
        parent.showback(true)

        parent.cardback.setOnClickListener {
            activity?.onBackPressed()
        }
    }    override fun onFragmentReady() {
        setupUi()
        var data = arrayListOf<SLiderModel>()
        data.add(SLiderModel(R.drawable.walk_2_r))
        data.add(SLiderModel(R.drawable.tst2))
        data.add(SLiderModel(R.drawable.tst3))
         loadSlider(data)
binding.lytNxt.setOnClickListener {
    if (PrefsHelper.getToken().isNullOrEmpty()) {
                findNavController().navigate(R.id.loginFirstDialog)

            } else {
    findNavController().navigate(R.id.basketFragment)
       }

}
        binding.cardback.setOnClickListener {

          findNavController().  popBackStack()
        }

           binding.cardNotify.setOnClickListener {

          findNavController().  navigate(R.id.notifactionFragment)
        }


    }

    private lateinit var handler: Handler
    private var urls: MutableList<SLiderModel> = mutableListOf()
    private fun loadSlider(mainSliders: ArrayList<SLiderModel>) {
        handler = Handler(Looper.myLooper()!!)
        if (mainSliders != null) {
            urls = mainSliders
            val adapter =
                activity?.let { SectionsHomePagerAdapter(  binding.viewpager, mainSliders) }
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