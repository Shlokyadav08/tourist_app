package com.example.citysightseeing

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (actionBar != null) {
            actionBar!!.hide() // Hide Action Bar
        }
        setContentView(R.layout.activity_main)

        // Delay for 3 seconds before switching to LoginActivity
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }, 3000)
    }
}
