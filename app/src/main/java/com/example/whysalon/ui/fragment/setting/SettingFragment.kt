package com.example.whysalon.ui.fragment.setting


import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.whysalon.R
import com.example.whysalon.base.BaseFragment
import com.example.whysalon.data.PrefsHelper
import com.example.whysalon.databinding.FragmentSettingBinding
import com.example.whysalon.ui.activity.MainActivity
import com.example.whysalon.util.ext.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>() {
    private lateinit var parent: MainActivity

    override fun onFragmentReady() {
        setupUi()
        binding.ivProfile.loadImage(resources.getDrawable(R.drawable.user_tst), isCircular = true)
        binding.headerData.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }
        binding.tvContactUs.setOnClickListener {
            findNavController().navigate(R.id.contactUsFragment)
        }
binding.btnLogout.setOnClickListener {
    findNavController().navigate(R.id.logoutDialog)
}
        if (PrefsHelper.getToken().isNullOrEmpty()){
            binding.headerEmptyState.isVisible= true
            binding.headerData.isVisible= false
            binding.btnLogin.isVisible= true
            binding.btnLogout.isVisible= false
        }else{
            binding.headerEmptyState.isVisible= false
            binding.headerData.isVisible= true
            binding.btnLogin.isVisible= false
            binding.btnLogout.isVisible= true


        }
    }

    private fun setupUi() {
        parent = requireActivity() as MainActivity
        parent.showBottomBar(true)
        parent.showToolbar(true)
        parent.showNotifactionFragment(false)
        parent.setTitle(resources.getString(R.string.setting))
        parent.showback(false)


    }
}