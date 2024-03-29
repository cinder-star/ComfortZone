@file:Suppress("PrivatePropertyName")

package com.comfortzone.user.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.comfortzone.user.R
import com.comfortzone.user.database.StorageUploadManager
import com.comfortzone.user.domains.PhotoOrder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
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
    private lateinit var customerName: TextView
    private lateinit var customerNumber: TextView
    private lateinit var customerAddresses: TextView
    private lateinit var confirmationText: RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_order)
        title = "বাজারের ছবি/প্রেসক্রিপশন আপলোড"
        bindWidgets()
        bindListeners()
    }

    private fun bindWidgets() {
        imageView = findViewById(R.id.photo_order)
        takePhotoButton = findViewById(R.id.take_photo)
        orderButton = findViewById(R.id.send_order)
        relativeLayout = findViewById(R.id.progress_bar_holder)
        progressBar = findViewById(R.id.upload_progress)
        customerName = findViewById(R.id.customer_name)
        customerAddresses = findViewById(R.id.customer_address)
        customerNumber = findViewById(R.id.customer_phone)
        confirmationText = findViewById(R.id.confirmation_holder)
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
                            "com.comfortzone.user",
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, IMAGE_CAPTURE_CODE)
                    }
                }
            }
        }

        orderButton.setOnClickListener {
            orderButton.hideKeyboard()
            if (isInternetAvailable()) {
                if (uploadName != null && validate()) {
                    relativeLayout.visibility = View.VISIBLE
                    progressBar.max = 100
                    val userId = Firebase.auth.currentUser!!.uid
                    val timeStamp: String = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
                    orderUpload(
                        userId,
                        timeStamp,
                        customerName.text.toString(),
                        customerAddresses.text.toString(),
                        customerNumber.text.toString()
                    )
                }
            } else {
                Toast.makeText(this, "No network connection!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun orderUpload(
        userId: String,
        timeStamp: String,
        name: String,
        address: String,
        number: String
    ) {
        val photoOrder = PhotoOrder(
            timeStamp,
            uploadName,
            userId,
            name,
            address,
            number
        )
        val database = Firebase.database.reference
        database.child("/order/$timeStamp").setValue(photoOrder)
            .addOnSuccessListener {
                fileUpload()
            }
            .addOnFailureListener {
                Toast.makeText(
                    this,
                    "Unexpected error occurred!\n Order could not be sent.",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    private fun fileUpload() {
        @Suppress("DEPRECATION")
        var bitmap = if (Build.VERSION.SDK_INT >= 29) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.contentResolver, uploadUri!!))
        } else {
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
        ).uploadFile(confirmationText)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CAPTURE_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    val file = File(currentPhotoPath)
                    uploadName = file.name
                    uploadUri = Uri.fromFile(file)
                    Picasso.get().load(file).into(imageView)
                } catch (e: Exception) {
                }
            }
        }
    }

    private fun validate(): Boolean {
        if (customerName.text.toString().isEmpty()) {
            customerName.error = "Field Cannot be empty"
            customerName.requestFocus()
            return false
        }
        if (customerAddresses.text.toString().isEmpty()) {
            customerAddresses.error = "Field Cannot be empty"
            customerAddresses.requestFocus()
            return false
        }
        val regex = "(01[356789][0-9]{8})".toRegex()
        val input = customerNumber.text.toString()
        if (!regex.matches(input)) {
            customerNumber.error = "Invalid Mobile Number!"
            return false
        }
        return true
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            timeStamp, /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    @Suppress("DEPRECATION")
    private fun isInternetAvailable(): Boolean {
        var result = false
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }

    private fun View.hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}