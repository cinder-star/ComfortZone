package com.sihan.comfortzone.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.FileProvider
import com.sihan.comfortzone.R
import com.squareup.picasso.Picasso
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class PhotoOrder : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var takePhotoButton: Button
    private lateinit var orderButton: Button
    private val IMAGE_CAPTURE_CODE: Int = 1
    private lateinit var currentPhotoPath: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_order)
        bindWidgets()
        bindListeners()
    }

    private fun bindWidgets() {
        imageView = findViewById(R.id.photo_order)
        takePhotoButton = findViewById(R.id.take_photo)
        orderButton = findViewById(R.id.send_order)
    }

    private fun bindListeners() {
        takePhotoButton.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(packageManager)?.also {
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        null
                    }
                    // Continue only if the File was successfully created
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            "com.sihan.comfortzone",
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, IMAGE_CAPTURE_CODE)
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CAPTURE_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    val file = File(currentPhotoPath)
                    val bitmap: Bitmap
                    bitmap = if (android.os.Build.VERSION.SDK_INT >= 29){
                        ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.contentResolver, Uri.fromFile(file)))
                    } else{
                        // Use older version
                        MediaStore.Images.Media.getBitmap(this.contentResolver, Uri.fromFile(file))
                    }
                    Picasso.get().load(file).into(imageView)
                } catch (e: Exception) {}
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }
}