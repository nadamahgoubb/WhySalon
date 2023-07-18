package com.example.whysalon.ui.fragment.walkThrought

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.whysalon.R
import com.example.whysalon.base.BaseFragment
import com.example.whysalon.databinding.FragmentWalkThroughtBinding
import com.example.whysalon.ui.activity.MainActivity
import com.example.whysalon.ui.fragment.walkThrough.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WalkThrougthFragment() : BaseFragment<FragmentWalkThroughtBinding>() {

    private var pos = 0
    override fun onFragmentReady() {
        setupViewPager()
        onClick()

    }


    private fun setupViewPager() {
         val adapter = SectionsPagerAdapter(this)
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ ->
            binding.viewPager.currentItem = 0
        }.attach()

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {

                super.onPageSelected(position)
                pos = position

                when (pos) {
                    0 -> {
binding.tvTitleSlider.setText(resources.getString(R.string.walk_title_1))

                    }
                    1 -> {
                        binding.tvTitleSlider.setText(resources.getString(R.string.walk_title_2))

                    }
                    2 -> {
                        binding.tvTitleSlider.setText(resources.getString(R.string.walk_title_3))

                    }
                }
            }
        })
    }

    private fun onClick() {
        binding.cardNext.setOnClickListener {
            if (pos == 0 || pos == 1)
                goNext()
            else
                gotoLMain()
        }



    }

    private fun goBack() {

        if (pos == 0) {
            activity?.onBackPressed()
        } else if (pos == 1 || pos == 2) {
            pos--
            binding.viewPager.currentItem = binding.viewPager.currentItem - 1
        }

    }

    private fun goNext() {
        if (pos == 0 || pos == 1) {
            pos++
            binding.viewPager.currentItem = binding.viewPager.currentItem + 1
        }
    }

    private fun gotoLMain() {
        findNavController().navigate(
            R.id.loginFragment,
            null,
            NavOptions.Builder().setPopUpTo(R.id.walkThroughtFragment, true).build()
        )
     //  findNavController().navigate(R.id.loginFragment)
     //   startActivity(Intent(activity, MainActivity::class.java))
     //   activity?.finish()
    }
}

