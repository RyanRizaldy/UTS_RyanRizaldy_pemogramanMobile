package com.example.utsryanrizaldy

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class EditMenu : AppCompatActivity() {
    lateinit var dishName: TextView
    lateinit var dishImage: ImageView

    lateinit var edit: Button
    lateinit var hapus: Button
    private lateinit var db: FirebaseFirestore

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_menu)

        dishName = findViewById(R.id.dishName)
        dishImage = findViewById(R.id.imageView)
        edit = findViewById(R.id.edit_button)
        hapus = findViewById(R.id.delete_button)
        db = FirebaseFirestore.getInstance()


        val intent = intent
        val id = intent.getStringExtra( "id")
        val title = intent.getStringExtra( "dishName")
        val imageUrl = intent.getStringExtra( "imageUrl")


        dishName.text = title
        Glide.with(this).load(imageUrl).into(dishImage)

        edit.setOnClickListener{
            val editIntent = Intent( this@EditMenu, AddMenu::class.java).apply {
                putExtra( "id", id)
                putExtra( "dishName", title)
                putExtra( "imageUrl", imageUrl)
            }
            startActivity(editIntent)
        }

        hapus.setOnClickListener{
            id?.let { documentId ->
                db.collection("Menu").document(documentId)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(this@EditMenu, "News deleted successfully",
                            Toast.LENGTH_SHORT).show()
                        val mainIntent = Intent( this@EditMenu, Dashboard::class.java).apply { addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        startActivity(mainIntent)
                        finish()
                    }

                    .addOnFailureListener { e ->
                        Toast.makeText( this@EditMenu, "Error deleting news: ${e.message}",
                            Toast.LENGTH_SHORT).show()
                        Log.w(  "NewsDetail",  "Error deleting document", e)
                    }
            }
        }
    }

}