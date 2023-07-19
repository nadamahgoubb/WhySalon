package com.example.whysalon.ui.fragment.setting


import androidx.navigation.fragment.findNavController
import com.example.whysalon.base.BaseFragment
import com.example.whysalon.databinding.FragmentChangePasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment : BaseFragment<FragmentChangePasswordBinding>() {
    override fun onFragmentReady() {
   binding.btnSubmit.setOnClickListener {

       findNavController().popBackStack()

   }  }

}