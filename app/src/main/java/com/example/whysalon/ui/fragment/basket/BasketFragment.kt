package com.example.whysalon.ui.fragment.basket

import androidx.navigation.fragment.findNavController
import com.example.whysalon.R
import com.example.whysalon.base.BaseFragment
import com.example.whysalon.databinding.FragmentBasketBinding
import com.example.whysalon.ui.activity.MainActivity
import kotlinx.android.synthetic.main.activity_main.cardback


class BasketFragment : BaseFragment<FragmentBasketBinding>() {
    private lateinit var parent: MainActivity

    private fun setupUi() {
        parent = requireActivity() as MainActivity
        parent.showBottomBar(false)
        parent.showToolbar(true)
        parent.setTitle(resources.getString(R.string.basket))
        parent.showback(true)

        parent.cardback.setOnClickListener {
            activity?.onBackPressed()
        }
    }    override fun onFragmentReady() {
        setupUi()
        onClick()
    }

    private fun onClick() {
        binding.btnGoservice.setOnClickListener {
            findNavController().navigate(R.id.servicesFragment)
        }
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.chooseBarberFragment)
        }
    }

}