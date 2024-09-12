package com.example.reead_write2

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {

    private val students = mutableListOf<Student>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val addButton: Button = findViewById(R.id.addButton)
        val readButton: Button = findViewById(R.id.readButton)
        val editText: EditText = findViewById(R.id.editText)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val studentAdapter = StudentAdapter(students)
        recyclerView.adapter = studentAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        val db = Firebase.firestore

        addButton.setOnClickListener {
            if(editText.text.isEmpty()) {
                Toast.makeText(this, "Wpisz imię", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            addButton.isEnabled = false
            // Add a new document with a generated id.
            val data = hashMapOf(
                "name" to editText.text.toString(),
                "country" to "Poland",
            )

            db.collection("students")
                .add(data)
                .addOnSuccessListener { documentReference ->
                    editText.text.clear()
                    addButton.isEnabled = true
                    Toast.makeText(this, "Dodano Studenta", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                    val student = Student(documentReference.id, data["name"].toString(), data["country"].toString())
                    students.add(student)
                    studentAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }
        readButton.setOnClickListener {
            students.clear()
            db.collection("students")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val student = Student(document.id, document.data["name"].toString(), document.data["country"].toString())
                        students.add(student)
                        Log.d(TAG, "${document.id} => ${document.data}")
                    }
                    studentAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                }
        }
    }
}