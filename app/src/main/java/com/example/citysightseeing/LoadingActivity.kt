package com.example.citysightseeing

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        val nextActivity = intent.getStringExtra("NEXT_ACTIVITY")

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = when (nextActivity) {
                "DashboardActivity" -> Intent(this, DashboardActivity::class.java)
                "JapanActivity" -> Intent(this, RegisterActivity::class.java)
                "SecondActivity" -> Intent(this, LoginActivity::class.java)
                else -> Intent(this, MainActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 800)
    }
}
