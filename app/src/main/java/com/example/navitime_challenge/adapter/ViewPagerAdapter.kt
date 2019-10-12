package com.example.navitime_challenge.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fm: FragmentManager, private val context: Context):
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    private val listFragments: MutableList<Fragment> = mutableListOf()
    private val listTitles: MutableList<String> = mutableListOf()

    override fun getItem(position: Int): Fragment {
        return listFragments.get(position)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return listTitles.get(position)
    }

    override fun getCount(): Int {
        return listTitles.size
    }

    fun addFragment(fragment: Fragment, title: String) {
        listFragments.add(fragment)
        listTitles.add(title)
    }
}
