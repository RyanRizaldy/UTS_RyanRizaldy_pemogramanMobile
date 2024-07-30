package com.example.utsryanrizaldy


import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Adapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File


class Dashboard : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var floatingActionButton: Button
    private lateinit var myAdapter: MenuAdapter
    private lateinit var itemList: MutableList<MenuItem>
    private lateinit var db: FirebaseFirestore
    private lateinit var progressDialog: ProgressDialog
    lateinit var Add: Button
    lateinit var logout: ImageView
    private lateinit var mAuth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        FirebaseApp.initializeApp(this)
        db = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()

        recyclerView = findViewById(R.id.rcvNews)
        floatingActionButton = findViewById(R.id.floatAddMenu)
        logout = findViewById(R.id.logout)

        logout.setOnClickListener {
            mAuth.signOut()
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@Dashboard, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        progressDialog = ProgressDialog(this@Dashboard).apply {
            setTitle("Loading...")
        }

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        itemList = ArrayList()
        myAdapter = MenuAdapter(itemList)
        recyclerView.adapter = myAdapter

        checkAdmin()
    }

    override fun onStart() {
        super.onStart()
        getData()
    }

    private fun checkAdmin() {
        val currentUser = mAuth.currentUser
        currentUser?.let { user ->
            Log.d("Dashboard", "Checking admin status for user: ${user.email}")
            db.collection("Admin").document(user.email!!)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        Log.d("Dashboard", "User is admin")
                        floatingActionButton.visibility = View.VISIBLE
                        floatingActionButton.setOnClickListener {
                            val toAddPage = Intent(this@Dashboard, AddMenu::class.java)
                            startActivity(toAddPage)
                        }

                        myAdapter.setOnItemClickListener(object : MenuAdapter.OnItemClickListener {
                            override fun onItemClick(item: MenuItem) {
                                val intent = Intent(this@Dashboard, EditMenu::class.java).apply {
                                    putExtra("id", item.id)
                                    putExtra("dishName", item.dishName)
                                    putExtra("imageUrl", item.imageUrl)
                                }
                                startActivity(intent)
                            }
                        })
                    } else {
                        Log.d("Dashboard", "User is not admin")
                        floatingActionButton.visibility = View.GONE
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("Dashboard", "Error checking admin status", exception)
                    floatingActionButton.visibility = View.GONE
                }
        }
    }

    private fun getData() {
        progressDialog.show()
        db.collection("Menu")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    itemList.clear()
                    for (document in task.result) {
                        val item = MenuItem(
                            document.id,
                            document.getString("dishName") ?: "",
                            document.getString("imgUrl") ?: ""
                        )
                        itemList.add(item)
                        Log.d("data", "${document.id} => ${document.data}")
                    }
                    myAdapter.notifyDataSetChanged()
                } else {
                    Log.w("data", "Error getting documents.", task.exception)
                }
                progressDialog.dismiss()
            }
    }

}


