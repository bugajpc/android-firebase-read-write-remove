package com.example.reead_write2

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class StudentAdapter(val students: MutableList<Student>) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
    val db = Firebase.firestore

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val textViewID: TextView = holder.itemView.findViewById(R.id.studentID)
        val textViewName: TextView = holder.itemView.findViewById(R.id.studentName)
        val textViewCountry: TextView = holder.itemView.findViewById(R.id.studentCountry)

        textViewID.text = students[position].id
        textViewName.text = students[position].name
        textViewCountry.text = students[position].country

        textViewName.setOnClickListener {
            db.collection("students").document(students[position].id)
                .delete()
                .addOnSuccessListener {
                    students.removeAt(position)
                    //notify that the number of items has changed
                    notifyItemRemoved(position)
                    //notify that the item was removed
                    notifyItemRangeChanged(position, students.size)
                    Log.d(TAG, "DocumentSnapshot successfully deleted!")
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
        }
    }

    override fun getItemCount(): Int {
        return students.size
    }
}