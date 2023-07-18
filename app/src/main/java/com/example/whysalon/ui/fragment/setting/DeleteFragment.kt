package com.example.whysalon.ui.fragment.setting

import android.content.Intent
import android.graphics.Paint
import androidx.core.content.ContextCompat
import com.example.whysalon.R
import com.example.whysalon.base.BaseFragment
import com.example.whysalon.data.PrefsHelper
import com.example.whysalon.databinding.FragmentDeleteBinding
import com.example.whysalon.ui.activity.AuthActivity
import com.example.whysalon.ui.activity.MainActivity
import com.example.whysalon.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
@AndroidEntryPoint
class DeleteFragment : BaseFragment<FragmentDeleteBinding>() {
    private lateinit var parent: MainActivity
var agreed = false
    override fun onFragmentReady() {
        setupUi()

    }

    private fun setupUi() {
        binding.btnKeepAccount.setPaintFlags(binding.btnKeepAccount.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        parent = requireActivity() as MainActivity
        parent.showBottomBar(false)
        parent.showToolbar(true)
        parent.showNotifactionFragment(false)
        parent.setTitle(resources.getString(R.string.delete_account))
        parent.showback(true)

parent.cardback.setOnClickListener {
    activity?.onBackPressed()
}
        binding.checkbox.setOnClickListener {
            if(binding.checkbox.isChecked==true){
                binding.btnDelete.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.red_dark)
                agreed = true
                //backgroundTintMode= resources.getColor(R.color.red_dark)
            }else{
                binding.btnDelete.backgroundTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.red_100)
                agreed = false

            }
        }
        binding.btnDelete.setOnClickListener {
            if(agreed) {

                PrefsHelper.clear()
                var intent = Intent(activity, AuthActivity::class.java)
                intent.putExtra(Constants.Start, Constants.login)
                startActivity(intent)
                activity?.finish()
            }
            else{
                showToast(getString(R.string.confirm_delete))
            }
        }
    }
}