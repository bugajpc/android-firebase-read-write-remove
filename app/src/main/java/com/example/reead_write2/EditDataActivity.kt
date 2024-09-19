package com.example.reead_write2

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore

class EditDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_data)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val studentId = intent.getStringExtra("id") ?: ""
        val nameEdit: EditText = findViewById(R.id.editDataEditText)
        val editButton: Button = findViewById(R.id.editDataButton)
        val db = Firebase.firestore

        val docRef = db.collection("students").document(studentId)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    nameEdit.setText(document.data!!["name"].toString())
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

        editButton.setOnClickListener {
            val data = hashMapOf(
                "name" to nameEdit.text.toString(),
                "country" to "USA"
            )

            db.collection("students").document(studentId)
                .set(data, SetOptions.merge())
                .addOnSuccessListener {
                    finish()
                }
        }
    }
}