package com.example.utsryanrizaldy

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Register : AppCompatActivity() {
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var username: EditText
    private lateinit var registerButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inisialisasi Firebase
        FirebaseApp.initializeApp(this)
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        username = findViewById(R.id.name)
        emailInput = findViewById(R.id.email)
        passwordInput = findViewById(R.id.password)
        registerButton = findViewById(R.id.register_button)
        progressBar = findViewById(R.id.progressBar)

        registerButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val name = username.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                emailInput.error = "Email is required"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                passwordInput.error = "Password is required"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(name)) {
                username.error = "Name is required"
                return@setOnClickListener
            }

            progressBar.visibility = View.VISIBLE

            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        val userId = mAuth.currentUser?.uid
                        val user = hashMapOf(
                            "name" to name,
                            "email" to email
                        )
                        userId?.let {
                            db.collection("users").document(it)
                                .set(user)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        this, "Register complete",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        this, "Error: ${e.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                        }
                    } else {
                        Toast.makeText(
                            this, "Registration Failed: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.v("error", task.exception?.message ?: "Unknown error")
                    }
                }
        }
    }
}
