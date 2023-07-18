package com.example.whysalon.ui.fragment.services

import androidx.navigation.fragment.findNavController
import com.example.whysalon.R
import com.example.whysalon.base.BaseFragment
import com.example.whysalon.databinding.FragmentServicesBinding
import com.example.whysalon.ui.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServicesFragment : BaseFragment<FragmentServicesBinding>() {
    private lateinit var parent: MainActivity

    override fun onFragmentReady() {
        setupUi()
        binding.item11.root.setOnClickListener {
            findNavController().navigate(R.id.serviceDetailsFragment)
        }
        binding.item12.root.setOnClickListener {
            findNavController().navigate(R.id.serviceDetailsFragment)
        }

    }

    private fun setupUi() {
        parent = requireActivity() as MainActivity
        parent.showBottomBar(true)
        parent.showToolbar(true)
        parent.setTitle(resources.getString(R.string.services))
        parent.showback(false)

    }
}