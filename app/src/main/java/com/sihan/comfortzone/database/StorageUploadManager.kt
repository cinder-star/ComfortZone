package com.sihan.comfortzone.database

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class StorageUploadManager(
    private val context: Context,
    path: String,
    private val data: ByteArray,
    private val progressBar: ProgressBar,
    private val relativeLayout: RelativeLayout,
    private val activity: Activity,
    private val successString: String,
    private val failureString: String
) {
    private val storageRef = Firebase.storage.reference
    private var fileRef: StorageReference

    init {
        fileRef = storageRef.child(path)
    }

    fun uploadFile(confirmationText: RelativeLayout) {
        fileRef.putBytes(data).addOnSuccessListener {
            relativeLayout.visibility = View.GONE
            Toast.makeText(context, successString, Toast.LENGTH_SHORT).show()
            confirmationText.visibility = View.VISIBLE
            Handler(Looper.myLooper()!!).postDelayed({
                activity.finish()
            }, 1000)
        }.addOnFailureListener {
            relativeLayout.visibility = View.GONE
            Toast.makeText(context, failureString, Toast.LENGTH_SHORT).show()
        }.addOnProgressListener {
            val progress = (100 * it.bytesTransferred) / it.totalByteCount
            progressBar.progress = progress.toInt()
        }
    }
}