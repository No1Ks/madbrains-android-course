package com.no1ks.madbrains_android_course.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.no1ks.madbrains_android_course.ui.fragment.FavouriteFragment
import com.no1ks.madbrains_android_course.ui.fragment.RepositoriesFragment

class FragmentsPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        var fragment = Fragment()
        when (position) {
            0 -> fragment =
                RepositoriesFragment()
            1 -> fragment =
                FavouriteFragment()
        }
        return fragment
    }

    override fun getItemCount(): Int = 2
}