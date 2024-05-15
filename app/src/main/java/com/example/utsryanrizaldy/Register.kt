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

class Register : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    companion object {
        var registeredUsername: String = ""
        var registeredPassword: String = ""
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val registerButton : Button = findViewById(R.id.register_button)
        registerButton.setOnClickListener {
            val nameEditText : EditText = findViewById(R.id.name)
            val name : String = nameEditText.text.toString()

            val emailEditText : EditText = findViewById(R.id.email)
            val email : String = emailEditText.text.toString()

            val passwordEditText : EditText = findViewById(R.id.password)
            val password : String = passwordEditText.text.toString()

         if (name.isEmpty() || email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Please fill the form", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

             Toast.makeText(this, "Registration Success", Toast.LENGTH_SHORT).show()
             val intent = Intent(this, MainActivity::class.java)
             startActivity(intent)

            registeredUsername = email
            registeredPassword = password
        }

        val loginLink = findViewById<TextView>(R.id.loginLink)
        loginLink.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        }
    }
