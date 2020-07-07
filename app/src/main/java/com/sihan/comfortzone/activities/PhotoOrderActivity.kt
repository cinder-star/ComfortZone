package com.sihan.comfortzone.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sihan.comfortzone.R
import com.sihan.comfortzone.database.DataWriteManager
import com.sihan.comfortzone.database.StorageUploadManager
import com.sihan.comfortzone.domains.PhotoOrder
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PhotoOrderActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var takePhotoButton: Button
    private lateinit var orderButton: Button
    private val IMAGE_CAPTURE_CODE: Int = 1
    private lateinit var currentPhotoPath: String
    private var uploadName: String? = null
    private var uploadUri: Uri? = null
    private lateinit var relativeLayout: RelativeLayout
    private lateinit var progressBar: ProgressBar
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
        relativeLayout = findViewById(R.id.progress_bar_holder)
        progressBar = findViewById(R.id.upload_progress)
    }

    @SuppressLint("SimpleDateFormat")
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

        orderButton.setOnClickListener {
            if (uploadName != null) {
                relativeLayout.visibility = View.VISIBLE
                progressBar.max = 100
                val userId = Firebase.auth.currentUser!!.uid
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                orderUpload(userId, timeStamp)
                fileUpload()
            }
        }
    }

    private fun orderUpload(userId: String, timeStamp: String) {
        DataWriteManager("photo_orders/$timeStamp", PhotoOrder(
            "orders/$uploadName",
            timeStamp,
            userId
        )).write()
    }

    private fun fileUpload() {
        var bitmap = if (android.os.Build.VERSION.SDK_INT >= 29){
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.contentResolver, uploadUri!!))
        } else{
            MediaStore.Images.Media.getBitmap(this.contentResolver, uploadUri)
        }
        val byteOutputStream = ByteArrayOutputStream()
        bitmap = Bitmap.createScaledBitmap(
            bitmap, 1024,
            ((bitmap.height * (1024.0 / bitmap.width)).toInt()), true
        )
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteOutputStream)
        val data = byteOutputStream.toByteArray()
        StorageUploadManager(
            this,
            "orders/$uploadName",
            data,
            progressBar,
            relativeLayout,
            this,
            "order placed successfully",
            "order placing failed"
        ).uploadFile()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CAPTURE_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    val file = File(currentPhotoPath)
                    val userId = Firebase.auth.currentUser!!.uid
                    val filename = file.name
                    uploadName = userId + filename
                    uploadUri = Uri.fromFile(file)
                    Picasso.get().load(file).into(imageView)
                } catch (e: Exception) {
                }
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