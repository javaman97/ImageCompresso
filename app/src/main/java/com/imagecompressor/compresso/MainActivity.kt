package com.imagecompressor.compresso

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var selectImageButton: Button
    private lateinit var compressImageButton: Button
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imageView = findViewById(R.id.imageView)
        selectImageButton = findViewById(R.id.selectBtn)
        compressImageButton = findViewById(R.id.compressBtn)

        selectImageButton.setOnClickListener {
            // Select an image from the gallery
            val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        compressImageButton.setOnClickListener {
            // Compress and display the selected image
            selectedImageUri?.let { uri ->
                compressImage(uri)?.let {
                    Glide.with(this).load(it).into(imageView)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            selectedImageUri = data?.data
            imageView.setImageURI(selectedImageUri)  // Display the selected image
        }
    }

    private fun compressImage(imageUri: Uri): File? {
        return try {
            val imageFile = File(imageUri.path)
            id.zelory.compressor.Compressor(this)
                .setQuality(75)
                .setMaxWidth(1024)
                .setMaxHeight(1024)
                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                .setDestinationDirectoryPath(cacheDir.absolutePath)
                .compressToFile(imageFile)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 100
    }

}