package com.example.whysalon.ui.fragment.services

import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.whysalon.R
import com.example.whysalon.base.BaseFragment
import com.example.whysalon.databinding.FragmentServiceMoreDetailsBinding
import com.example.whysalon.ui.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_more_details.view.*

@AndroidEntryPoint
class ServiceMoreDetailsFragment : BaseFragment<FragmentServiceMoreDetailsBinding>() {
    private lateinit var parent: MainActivity
    var state = 0
    override fun onFragmentReady() {
        setupUi()

        binding.item12.checkbox.setOnClickListener {
            if (binding.item12.checkbox.isChecked) {
                enablebtn(true)
                binding.item12.your_rectangle.setBackgroundResource(R.drawable.bg_btn_white_black_border)
                binding.item11.your_rectangle.setBackgroundResource(R.drawable.bg_btn_white_grey_border)
                binding.item11.checkbox.isChecked= false

            } else {
                enablebtn(false)
                binding.item12.your_rectangle.setBackgroundResource(R.drawable.bg_btn_white_grey_border)


            }
        }
        binding.item11.checkbox.setOnClickListener {
            if (binding.item11.checkbox.isChecked) {
                enablebtn(true)
                binding.item11.your_rectangle.setBackgroundResource(R.drawable.bg_btn_white_black_border)
                binding.item12.your_rectangle.setBackgroundResource(R.drawable.bg_btn_white_grey_border)
                binding.item12.checkbox.isChecked= false

            } else {
                enablebtn(false)
                binding.item11.your_rectangle.setBackgroundResource(R.drawable.bg_btn_white_grey_border)


            }
        }

    }

    fun enablebtn(enable: Boolean) {
        if (enable) {
            binding.lytNxt.background = resources.getDrawable(R.drawable.bg_btn_black_white_border)
             state = 1
        } else {
            binding.lytNxt.background = resources.getDrawable(R.drawable.bg_btn_gray)
             state = 0

        }
    }

    private fun setupUi() {
        parent = requireActivity() as MainActivity
        parent.showBottomBar(false)
        parent.showToolbar(true)
        parent.showback(true)

        parent.cardback.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.lytNxt.setOnClickListener {
            if(state==1) findNavController().navigate(R.id.chooseBarberFragment)
        }
        parent.setTitle(resources.getString(R.string.hair_cut))

    }
}