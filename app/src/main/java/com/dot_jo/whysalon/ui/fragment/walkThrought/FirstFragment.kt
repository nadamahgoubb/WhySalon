package com.dot_jo.whysalon.ui.fragment.walkThrough

 import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseFragment
import com.dot_jo.whysalon.databinding.ItemWalkthrougthBinding
import com.dot_jo.whysalon.util.ext.loadImage


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