package com.example.whysalon.ui.fragment.setting

import com.example.whysalon.R
import com.example.whysalon.base.BaseFragment
import com.example.whysalon.databinding.FragmentContactUsBinding
import com.example.whysalon.ui.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class ContactUsFragment : BaseFragment<FragmentContactUsBinding>() {
         private lateinit var parent: MainActivity

        override fun onFragmentReady() {
            setupUi()

     }

    private fun setupUi() {
        parent = requireActivity() as MainActivity
        parent.showBottomBar(false)
        parent.showToolbar(true)
        parent.showNotifactionFragment(false)
        parent.setTitle(resources.getString(R.string.contact_us))
        parent.showback(true)

        parent.cardback.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}