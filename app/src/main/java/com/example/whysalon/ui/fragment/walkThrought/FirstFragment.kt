package com.example.whysalon.ui.fragment.walkThrough

 import com.example.whysalon.R
import com.example.whysalon.base.BaseFragment
import com.example.whysalon.databinding.ItemWalkthrougthBinding
import com.example.whysalon.util.ext.loadImage


class FirstFragment(var state: Int) : BaseFragment<ItemWalkthrougthBinding>() {


    private fun setUpUi() {

        when (state) {
            0 -> {
                binding.imgSlider.loadImage(R.mipmap.walk_1)
            }
            1 -> {
                binding.imgSlider.loadImage(R.drawable.walk_2_r)
            }
            2 -> {
                binding.imgSlider.loadImage(R.mipmap.walk_3)

            }

        }

    }

    override fun onFragmentReady() {
        setUpUi()
    }
}