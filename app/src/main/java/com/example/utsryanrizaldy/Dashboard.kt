package com.example.utsryanrizaldy


import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class Dashboard : AppCompatActivity() {
    private lateinit var lv: ListView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        lv = findViewById(R.id.listView)

        val menus: Array<MenuClass> = arrayOf(
            MenuClass("Lemmon Poppy Seed Pancake and Berry", R.drawable.pancake),
            MenuClass("Salmon Burger With Harrisa Carrot", R.drawable.steak),
            MenuClass("Dill-Chimicuhurri Shrimp With Roasted Vegetables", R.drawable.salad)
        )

        val adapter = MenuAdapter(this, R.layout.menu_list, menus)
        lv.adapter = adapter

    }
}
//val add : Button = findViewById(R.id.add_to_cart)
//add.setOnClickListener {
//    Toast.makeText(this, "added to cart", Toast.LENGTH_SHORT).show()
//}
