package com.example.navitime_challenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.navitime_challenge.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var adapter: ViewPagerAdapter

    lateinit var mFirestore: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Firestore
        FirebaseFirestore.setLoggingEnabled(true)
        mFirestore = FirebaseFirestore.getInstance()

        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.pager)
        adapter = ViewPagerAdapter(supportFragmentManager, this)

        // Add fragments
        adapter.addFragment(FragmentTab01(), "Home")
        adapter.addFragment(FragmentTab02(), "OrderList")
        adapter.addFragment(FragmentTab03(), "OrderMap")

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(pager)
    }
}
