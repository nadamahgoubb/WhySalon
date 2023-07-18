package com.example.whysalon.ui.fragment.profile


import android.graphics.Paint
import androidx.navigation.fragment.findNavController
import com.example.whysalon.R
import com.example.whysalon.base.BaseFragment
import com.example.whysalon.databinding.FragmentProfileBinding
import com.example.whysalon.ui.activity.MainActivity
import com.example.whysalon.util.ext.loadImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    private lateinit var parent: MainActivity
    override fun onFragmentReady() {
setupUi()
    onClick()}

    private fun onClick() {
        binding.btnDeletAccount.setOnClickListener {
            findNavController().navigate(R.id.deleteFragment)
        }
        binding.tvChangePass.setOnClickListener {
            findNavController().navigate(R.id.changePasswordFragment)
        }
    }

    private fun setupUi() {
        parent = requireActivity() as MainActivity
        parent.showBottomBar(false)
        parent.showToolbar(true)
        parent.setTitle(resources.getString(R.string.profile))

        parent.showNotifactionFragment(false)
        parent.showback(true)

        parent.cardback.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.ivProfile.loadImage(resources.getDrawable(R.drawable.user_tst), isCircular = true)
        binding.tvChangePass.setPaintFlags(binding.tvChangePass.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
    }
}