package com.example.whysalon.ui.fragment


import androidx.core.view.isVisible
import com.example.whysalon.R
import com.example.whysalon.base.BaseFragment
import com.example.whysalon.databinding.FragmentNotifactionBinding
import com.example.whysalon.ui.activity.MainActivity
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
        parent.showNotifactionFragment(false)
        parent.setTitle(resources.getString(R.string.notification))
        parent.showback(true)

        parent.cardback.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}