package com.example.citysightseeing

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DashboardActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var fabMic: FloatingActionButton
    private lateinit var fabOpen: Animation
    private lateinit var fabClose: Animation
    private var isFabOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        bottomNavigation = findViewById(R.id.bottomNavigation)
        fabMic = findViewById(R.id.fabMic)

        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open)
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close)

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> startActivity(Intent(this, HomeActivity::class.java))
                R.id.nav_calendar -> startActivity(Intent(this, CalendarActivity::class.java))
                R.id.nav_messages -> startActivity(Intent(this, MessagesActivity::class.java))
                R.id.nav_profile -> startActivity(Intent(this, ProfileActivity::class.java))
            }
            true
        }

        fabMic.setOnClickListener {
            toggleFab()
        }
    }

    private fun toggleFab() {
        if (isFabOpen) {
            fabMic.startAnimation(fabClose)
        } else {
            fabMic.startAnimation(fabOpen)
        }
        isFabOpen = !isFabOpen
    }
}
