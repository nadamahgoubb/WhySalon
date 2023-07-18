package com.example.whysalon.ui.fragment.services


import androidx.navigation.fragment.findNavController
import com.example.whysalon.R
import com.example.whysalon.base.BaseFragment
import com.example.whysalon.data.PrefsHelper
import com.example.whysalon.databinding.FragmentServiceDetailsBinding
import com.example.whysalon.databinding.FragmentServicesBinding
import com.example.whysalon.ui.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class ServiceDetailsFragment : BaseFragment<FragmentServiceDetailsBinding>() {
    private lateinit var parent: MainActivity

    override fun onFragmentReady() {
        setupUi()
        binding.item11.root.setOnClickListener {
            if (PrefsHelper.getToken().isNullOrEmpty()) {
                findNavController().navigate(R.id.loginFirstDialog)

            } else {
                findNavController().navigate(R.id.serviceMoreDetailsFragment)
            }
        }
        binding.item12.root.setOnClickListener {
            if (PrefsHelper.getToken().isNullOrEmpty()) {
                findNavController().navigate(R.id.loginFirstDialog)

            } else {
                findNavController().navigate(R.id.serviceMoreDetailsFragment)
            }        }

    }

    private fun setupUi() {
        parent = requireActivity() as MainActivity
        parent.showBottomBar(false)
        parent.showToolbar(true)
         parent.setTitle(resources.getString(R.string.hair_services))
        parent.showback(true)

        parent.cardback.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}