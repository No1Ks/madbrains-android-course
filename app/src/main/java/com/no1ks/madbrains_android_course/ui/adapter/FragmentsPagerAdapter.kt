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
        return when (position) {
            0 -> RepositoriesFragment()
            1 -> FavouriteFragment()
            else -> Fragment()
        }
    }

    override fun getItemCount(): Int = 2
}