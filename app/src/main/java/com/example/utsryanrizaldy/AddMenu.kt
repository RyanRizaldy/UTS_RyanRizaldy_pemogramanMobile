package com.example.utsryanrizaldy

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.bumptech.glide.Glide

class AddMenu : AppCompatActivity() {
    private var id: String? = ""
    private var dishName: String? = null
    private var image: String? = null

    private val PICK_IMAGE_REQUEST = 1

    private lateinit var dishNameInput: EditText
    private lateinit var imageView: ImageView
    private lateinit var saveMenu: Button
    private lateinit var chooseImage: Button
    private var imageUri: Uri? = null

    private lateinit var dbNews: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var progressDialog: ProgressDialog


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        dbNews = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        // Initialize UI components
        dishNameInput = findViewById(R.id.dishNameInput)
        imageView = findViewById(R.id.imageView)
        saveMenu = findViewById(R.id.registermenu_button)
        chooseImage = findViewById(R.id.addimg_button)

        val updateOption = intent
        if (updateOption != null) {
            id = updateOption.getStringExtra( "id")
            dishName = updateOption.getStringExtra(  "dishName")
            image = updateOption.getStringExtra(  "imageUrl")

            dishNameInput.setText(dishName)
            Glide.with ( this).load(image). into (imageView)
        }

        progressDialog = ProgressDialog(this@AddMenu).apply {
            setTitle("Loading....")
        }

        chooseImage.setOnClickListener{
            openFileChooser()
        }

        saveMenu.setOnClickListener{
            val dishName = dishNameInput.text.toString().trim()


            if (dishName.isEmpty()) {
                Toast.makeText(this@AddMenu, "Dish Name cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            progressDialog.show()
            if (imageUri != null ){
                uploadImageToStorage(dishName)
            } else {
                saveData(dishName,image ?: "")
            }
        }



    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null ){
            imageUri = data.data
            imageView.setImageURI(imageUri)
        }
    }

    private fun uploadImageToStorage (newsTitle: String) {
        imageUri?.let { uri ->
            val storageRef =
                storage.reference.child("Menu_images/" + System.currentTimeMillis() + ".jpg")
            storageRef.putFile(uri)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        val imageUrl = downloadUri.toString()
                        saveData(newsTitle, imageUrl)
                    }
                }
                .addOnFailureListener { e ->
                    progressDialog.dismiss()
                    Toast.makeText(
                        this@AddMenu,
                        "Failed to upload image: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()

                }
        }
    }

    private fun saveData (dishName: String, imageUrl: String) {
        val menu = HashMap<String, Any>()
        menu["dishName"] = dishName
        menu["imgUrl"]= imageUrl

        if (id != null) {
            dbNews.collection("Menu")
                .document(id?:"")
                .update(menu)
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    Toast.makeText(this@AddMenu, "Menu added successfully", Toast.LENGTH_SHORT).show()
                    dishNameInput.setText("")
                    imageView.setImageResource(0)
                    goToDaschboard()
                }
                .addOnFailureListener { e ->
                    progressDialog.dismiss()
                    Toast.makeText(
                        this@AddMenu,
                        "Error updating menu: ${e.message}",
                        Toast.LENGTH_SHORT).show()
                    Log.w("AddMenu", "Error updating document ", e)
                }

        } else {
            dbNews.collection("Menu")
                .add(menu)
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    Toast.makeText(this@AddMenu, "Menu added successfully", Toast.LENGTH_SHORT).show()
                    dishNameInput.setText("")
                    imageView.setImageResource(0)
                    goToDaschboard()
                }
                .addOnFailureListener { e ->
                    progressDialog.dismiss()
                    Toast.makeText(
                        this@AddMenu,
                        "Error adding menu: ${e.message}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    Log.w("AddMenu", "Error adding document ", e)
                }

        }


    }
    private fun goToDaschboard(){
        val intent=Intent(this,Dashboard::class.java)
        startActivity(intent)
    }
}