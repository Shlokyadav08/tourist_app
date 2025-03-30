package com.example.citysightseeing

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etNewPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnResetPassword: Button
    private lateinit var ivEyeNewPassword: ImageView
    private lateinit var auth: FirebaseAuth

    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        auth = FirebaseAuth.getInstance()

        etEmail = findViewById(R.id.etEmail)
        etNewPassword = findViewById(R.id.etNewPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnResetPassword = findViewById(R.id.btnResetPassword)
        ivEyeNewPassword = findViewById(R.id.ivEyeNewPassword)

        ivEyeNewPassword.setOnClickListener {
            togglePasswordVisibility()
        }

        btnResetPassword.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val newPassword = etNewPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            if (validateEmail(email) && validatePassword(newPassword, confirmPassword)) {
                checkIfEmailExists(email)
            }
        }

        etNewPassword.addTextChangedListener(passwordWatcher)
        etConfirmPassword.addTextChangedListener(passwordWatcher)
    }
    // todo Create a class validator not use in a file
    // todo mistake a var user FirebasseAuth not var use
    // run project

    private fun validateEmail(email: String): Boolean {
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
        return true
    }

    private fun validatePassword(newPassword: String, confirmPassword: String): Boolean {
        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Both fields are required", Toast.LENGTH_SHORT).show()
            return false
        }
        if (newPassword.length < 8) {
            Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!newPassword.matches(".*[A-Z].*".toRegex())) {
            Toast.makeText(this, "Password must contain at least one uppercase letter", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!newPassword.matches(".*[0-9].*".toRegex())) {
            Toast.makeText(this, "Password must contain at least one number", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!newPassword.matches(".*[!@#\$%^&*].*".toRegex())) {
            Toast.makeText(this, "Password must contain at least one special character (!@#\$%^&*)", Toast.LENGTH_SHORT).show()
            return false
        }
        if (newPassword != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun checkIfEmailExists(email: String) {
        val addOnCompleteListener = auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val signInMethods = task.result?.signInMethods ?: emptyList()
                    Log.d("FirebaseAuth", "Sign-in methods for $email: $signInMethods")

                    if (signInMethods.isNotEmpty()) {
                        // ✅ Email exists
                        Toast.makeText(this, "Email exists. Proceeding...", Toast.LENGTH_SHORT)
                            .show()
                        sendOtpForVerification()
                    } else {
                        // ❌ Email does not exist
                        Toast.makeText(this, "Email is not registered!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("FirebaseAuth", "Error checking email: ${task.exception?.message}")
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        val inputType = if (isPasswordVisible) InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        else InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        etNewPassword.inputType = inputType
        etNewPassword.setSelection(etNewPassword.text.length)
        ivEyeNewPassword.setImageResource(if (isPasswordVisible) R.drawable.ic_eye_open else R.drawable.ic_eye_closed)
    }

    private val passwordWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            btnResetPassword.isEnabled = etNewPassword.text.toString().isNotEmpty() && etConfirmPassword.text.toString().isNotEmpty()
        }
        override fun afterTextChanged(s: Editable?) {}
    }

    private fun sendOtpForVerification() {
        Toast.makeText(this, "Check your email for the reset link!", Toast.LENGTH_LONG).show()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
//        startActivity(intent)
    }
}
