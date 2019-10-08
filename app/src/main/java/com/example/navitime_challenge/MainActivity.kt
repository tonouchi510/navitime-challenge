package com.example.navitime_challenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val texts = arrayOf("abc ", "bcd", "cde", "def", "efg",
        "fgh", "ghi", "hij", "ijk", "jkl", "klm")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        pager.adapter = TabAdapter(supportFragmentManager,this)
        tabLayout.setupWithViewPager(pager)
    }
}
