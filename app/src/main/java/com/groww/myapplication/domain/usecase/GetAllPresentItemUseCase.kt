package com.groww.myapplication.domain.usecase

import com.google.firebase.database.DatabaseReference
import com.groww.myapplication.data.ItemModel
import com.groww.myapplication.domain.repository.Repository
import com.groww.myapplication.presenter.ui.main.RecyclerViewAdapter

class GetAllPresentItemUseCase (private val repository: Repository){

    suspend fun execute(recyclerViewAdapter: RecyclerViewAdapter, database: DatabaseReference){
        return repository.getAllItem(recyclerViewAdapter,database)

    }
}