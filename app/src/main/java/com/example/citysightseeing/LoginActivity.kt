package com.example.citysightseeing

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

class LoginActivity : AppCompatActivity() {
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        val tvSignup = findViewById<TextView>(R.id.tvSignup)
        val tvForgotPassword = findViewById<TextView>(R.id.fp)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val ivEye = findViewById<ImageView>(R.id.ivEye)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        val spannable = SpannableString("Don't have an account? Sign Up")
        spannable.setSpan(ForegroundColorSpan(Color.BLACK), 0, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.deep_blue)),
            24,
            spannable.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        tvSignup.text = spannable

        tvSignup.setOnClickListener { v: View? ->
            startActivity(
                Intent(
                    this,
                    RegisterActivity::class.java
                )
            )
        }
        tvForgotPassword.setOnClickListener { v: View? ->
            startActivity(
                Intent(
                    this,
                    ForgotPasswordActivity::class.java
                )
            )
        }

        val isPasswordVisible = booleanArrayOf(false)
        ivEye.setOnClickListener { v: View? ->
            isPasswordVisible[0] = !isPasswordVisible[0]
            etPassword.transformationMethod =
                if (isPasswordVisible[0]) HideReturnsTransformationMethod.getInstance() else PasswordTransformationMethod.getInstance()
            ivEye.setImageResource(if (isPasswordVisible[0]) R.drawable.ic_eye_open else R.drawable.ic_eye_closed)
            etPassword.setSelection(etPassword.text.length)
        }

        btnLogin.setOnClickListener { v: View? ->
            val email =
                etEmail.text.toString().trim { it <= ' ' }.lowercase(Locale.getDefault())
            val password = etPassword.text.toString().trim { it <= ' ' }
            if (validateInputs(etEmail, etPassword)) {
                loginUser(email, password)
            }
        }
    }

    private fun validateInputs(etEmail: EditText, etPassword: EditText): Boolean {
        val email = etEmail.text.toString().trim { it <= ' ' }
        val password = etPassword.text.toString().trim { it <= ' ' }

        if (email.isEmpty()) {
            etEmail.error = "Email is required!"
            etEmail.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Enter a valid email!"
            etEmail.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            etPassword.error = "Password is required!"
            etPassword.requestFocus()
            return false
        }

        if (password.length < 6) {
            etPassword.error = "Password must be at least 6 characters!"
            etPassword.requestFocus()
            return false
        }

        return true
    }

    private fun loginUser(email: String, password: String) {
        auth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, DashboardActivity::class.java))
                    finish()
                } else {
                    val errorMessage = task.exception?.message ?: "Authentication failed"
                    Log.e("FirebaseAuth", "Login failed: $errorMessage")
                    Toast.makeText(this, "Login Failed: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }

    }
}