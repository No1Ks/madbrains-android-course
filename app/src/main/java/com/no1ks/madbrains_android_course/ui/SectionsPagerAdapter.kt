package com.no1ks.madbrains_android_course.ui

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.no1ks.madbrains_android_course.R

private val TAB_TITLES = arrayOf(
    R.string.tab_all,
    R.string.tab_favourite
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment = Fragment()
        when (position) {
            0 -> fragment =
                RepositoriesFragment()
            1 -> fragment =
                FavouriteFragment()
        }
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 2
    }
}