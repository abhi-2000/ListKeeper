package com.groww.myapplication.presenter.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseReference
import com.groww.myapplication.data.ItemModel
import com.groww.myapplication.domain.usecase.AddNewItemUseCase
import com.groww.myapplication.domain.usecase.GetAllPresentItemUseCase
import com.groww.myapplication.domain.usecase.RemoveCompletedItemUseCase
import com.groww.myapplication.presenter.ui.main.RecyclerViewAdapter
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class BaseViewModel(
    private val addNewItemUseCase: AddNewItemUseCase,
    private val removeCompletedItemUseCase: RemoveCompletedItemUseCase,
    private val getAllPresentItemUseCase: GetAllPresentItemUseCase
) : ViewModel() {

    fun writeNewItem(database: DatabaseReference, title: String, description: String, id: Int) {
        viewModelScope.launch{
            addNewItemUseCase.execute(database,title,description, id)
        }
    }

    fun deleteItem(){
        removeCompletedItemUseCase.execute()
    }

    suspend fun getAllItems(recyclerViewAdapter: RecyclerViewAdapter, database: DatabaseReference) {
        return viewModelScope.async {
            getAllPresentItemUseCase.execute(recyclerViewAdapter,database)
        }.await()
    }

}