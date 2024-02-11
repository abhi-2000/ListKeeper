package com.groww.myapplication.domain.repository

import com.google.firebase.database.DatabaseReference
import com.groww.myapplication.data.ItemModel
import com.groww.myapplication.presenter.ui.main.RecyclerViewAdapter

interface Repository {

    suspend fun writeNewItem(database: DatabaseReference, title: String, description: String, id: Int)

    suspend fun getAllItem(recyclerViewAdapter: RecyclerViewAdapter, database: DatabaseReference)

    suspend fun removeCompletedItem()
}