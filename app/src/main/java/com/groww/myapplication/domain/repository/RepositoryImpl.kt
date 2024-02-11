package com.groww.myapplication.domain.repository

import android.util.Log
import com.google.firebase.FirebaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.groww.myapplication.data.ItemModel
import com.groww.myapplication.presenter.ui.main.RecyclerViewAdapter

class RepositoryImpl : Repository {
    override suspend fun writeNewItem(
        database: DatabaseReference,
        title: String,
        description: String,
        id: Int
    ) {
        val item = ItemModel(id, title, description)
        val newDataRef = database.push()

//        newDataRef.child("title").setValue(title)
//        newDataRef.child("description").setValue(description)

        newDataRef.setValue(item)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("Repository", "writeNewUser: success")
                } else {
                    Log.d("Repository", "writeNewUser: failure")
                }
            }
    }

    override suspend fun getAllItem(
        recyclerViewAdapter: RecyclerViewAdapter,
        database: DatabaseReference
    ) {
        val list: MutableList<ItemModel> = mutableListOf()

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach { childSnapshot ->
                    // Extract the title and description from each child
                    val id = childSnapshot.child("id").getValue(Int::class.java)
                    val title = childSnapshot.child("title").getValue(String::class.java)
                    val description =
                        childSnapshot.child("description").getValue(String::class.java)

                    val item = ItemModel(id, title, description)
                    // Print or process the extracted title and description
                    println("Title: $title")
                    println("Description: $description")
                    list.add(item)
                    // Process data updates similar to onDataChange above
                }

                Log.d("ABHI", "onCreate: ${list.toList()}")
                recyclerViewAdapter.differ.submitList(list.toList())
                recyclerViewAdapter.notifyDataSetChanged()
                list.clear()

            }

            override fun onCancelled(error: DatabaseError) {
                println("Database Error: ${error.message}")
            }
        })
    }

    override suspend fun removeCompletedItem() {
        TODO("Not yet implemented")
    }
}