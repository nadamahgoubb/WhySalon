package com.example.whysalon.ui.fragment.walkThrough

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.whysalon.ui.fragment.walkThrought.WalkThrougthFragment


class SectionsPagerAdapter(fragmentActivity: WalkThrougthFragment) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return FirstFragment(0)
            1 -> return FirstFragment(1)
            2 -> return FirstFragment(2)
        }
        return FirstFragment(-1)
    }
}