package com.example.whysalon.ui.fragment.booking

import androidx.navigation.fragment.findNavController
import com.example.whysalon.R
import com.example.whysalon.base.BaseFragment
import com.example.whysalon.databinding.FragmentBookingBinding
import com.example.whysalon.ui.activity.MainActivity

class BookingFragment : BaseFragment<FragmentBookingBinding>() {
    private lateinit var parent: MainActivity

    override fun onFragmentReady() {
        setupUi()
        binding.lytCancel.setOnClickListener {
            findNavController().navigate(R.id.cancelBookingDialog)
        }
    }

    private fun setupUi() {
        parent = requireActivity() as MainActivity
        parent.showBottomBar(true)
        parent.showToolbar(true)
        parent.showNotifactionFragment(false)
        parent.showback(false)
        parent.setTitle(resources.getString(R.string.booking))
        binding.btnHistory.setOnClickListener {
            findNavController().navigate(R.id.historyFragment)
        }
    }

}