package com.example.citysightseeing

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        auth = FirebaseAuth.getInstance()

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val btnReset = findViewById<Button>(R.id.btnReset)
        val tvBackToLogin = findViewById<TextView>(R.id.tvBackToLogin)

        btnReset.setOnClickListener {
            val email = etEmail.text.toString().trim()

            if (email.isEmpty()) {
                etEmail.error = "Enter your email"
                etEmail.requestFocus()
            } else {
                checkIfEmailExists(email)
            }
        }

        tvBackToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun checkIfEmailExists(email: String) {
        val normalizedEmail = email.trim().lowercase()
        Log.d("FirebaseAuth", "🔍 Checking email existence for: '$normalizedEmail'")

        auth.fetchSignInMethodsForEmail(normalizedEmail)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val signInMethods = task.result?.signInMethods ?: emptyList()
                    Log.d("FirebaseAuth", "Sign-in methods: $signInMethods")

                    if (signInMethods.isNotEmpty()) {
                        Log.d("FirebaseAuth", "✅ Email exists!")
                        Toast.makeText(this, "✅ Email exists!", Toast.LENGTH_SHORT).show()

                        // Proceed with sending password reset email
                        sendPasswordResetEmail(normalizedEmail)
                    } else {
                        Log.e("FirebaseAuth", "❌ Email not registered")
                        Toast.makeText(this, "✅ Email exists!", Toast.LENGTH_SHORT).show()
                        sendPasswordResetEmail(normalizedEmail)
                    }
                } else {
                    val errorMessage = task.exception?.message ?: "Unknown error"
                    Log.e("FirebaseAuth", "❗ Error checking email: $errorMessage")
                    Toast.makeText(this, "⚠️ Error: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Password reset link sent to $email", Toast.LENGTH_LONG).show()

                    // Navigate to OTP verification
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Failed to send reset email", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
