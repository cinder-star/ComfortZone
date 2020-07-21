package com.comfortzone.user.database

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.comfortzone.user.domains.Feedback

class WriteFeedback(private var path: String, private var feedback: Feedback) {
    fun write(uid: String, id: String) {
        val db = Firebase.firestore
        db.collection(path)
            .document(uid)
            .collection(id)
            .add(feedback)
            .addOnSuccessListener {
                Log.e("feedback", "written")
            }
            .addOnFailureListener { e ->
                Log.e("feedback", e.toString())
            }
    }
}