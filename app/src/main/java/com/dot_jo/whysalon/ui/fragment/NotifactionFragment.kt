package com.dot_jo.whysalon.ui.fragment


import androidx.core.view.isVisible
import com.dot_jo.whysalon.base.BaseFragment
import com.dot_jo.whysalon.databinding.FragmentNotifactionBinding
import com.dot_jo.whysalon.ui.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class NotifactionFragment : BaseFragment<FragmentNotifactionBinding>() {

    private lateinit var parent: MainActivity

    override fun onFragmentReady() {
        setupUi()
binding.item11.ivOn.isVisible= true
binding.item12.ivOn.isVisible= true
    }

    private fun setupUi() {
        parent = requireActivity() as MainActivity
        parent.showBottomBar(false)
        parent.showToolbar(true)
        parent.showNotifactionFragment(true)

        parent.cardback.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}