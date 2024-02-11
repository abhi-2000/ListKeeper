package com.groww.myapplication.domain.usecase

import com.google.firebase.database.DatabaseReference
import com.groww.myapplication.domain.repository.Repository

class AddNewItemUseCase(private val repository: Repository) {

    suspend fun execute(database: DatabaseReference, title: String, description: String, id: Int) =
        repository.writeNewItem(database, title, description, id)
}