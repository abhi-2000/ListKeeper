package com.groww.myapplication.presenter.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.groww.myapplication.R
import com.groww.myapplication.data.ItemModel
import com.groww.myapplication.databinding.ItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class RecyclerViewAdapter(private val databaseRef: DatabaseReference) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private val callBack = object : DiffUtil.ItemCallback<ItemModel>() {
        override fun areItemsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ItemModel, newItem: ItemModel): Boolean {
            return oldItem == newItem
        }
    }

    var differ = AsyncListDiffer(this, callBack)

    inner class ViewHolder(private val binding: ItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemModel: ItemModel?, position: Int) {
            val currentItem: ItemModel = differ.currentList[position]
            binding.titleText.text = itemModel?.title
            binding.bodyText.text = itemModel?.description
            binding.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {

                    CoroutineScope(Dispatchers.IO).launch {
                        delay(100)
                    }
                    val applesQuery: Query =
                        databaseRef.orderByChild("id").equalTo(currentItem.id.toString())

                    applesQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (snapshot in dataSnapshot.children) {
                                // Get the reference to the node
                                val nodeReference = snapshot.ref
                                // Delete the node
                                nodeReference.removeValue().addOnSuccessListener {
                                    println("Data deleted successfully")
                                }.addOnFailureListener {
                                    println("Failed to delete data: ${it.message}")
                                }
                            }

//                    databaseRef.child(currentItem.id.toString()).removeValue()
//                        .addOnSuccessListener {
//                            Log.d("DELETE", "success")
//                        }
//                        .addOnFailureListener { e ->
//                            println("Error deleting data: ${e.message}")
//                        }

                            val currentList = differ.currentList.toMutableList()
                            val indexToRemove = currentList.indexOf(currentItem)
                            if (indexToRemove != -1) {
                                currentList.removeAt(indexToRemove)
                                differ.submitList(currentList)
                                notifyItemRemoved(indexToRemove)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: ItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position], position)
    }

    override fun getItemCount(): Int {
        Log.d("Adapter", "getItemCount: ${differ.currentList.size}")
        return differ.currentList.size
    }

}

