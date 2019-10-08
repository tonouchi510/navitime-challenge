package com.example.navitime_challenge

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TabAdapter(fm: FragmentManager, private val context: Context): FragmentPagerAdapter(fm){

    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> { return FragmentTab01() }
            1 -> { return FragmentTab02() }
            else ->  { return FragmentTab03() }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0 -> { return "Home" }
            1 -> { return "OrderList" }
            else ->  { return "OrderMap" }
        }
    }

    override fun getCount(): Int {
        return 3
    }
}
